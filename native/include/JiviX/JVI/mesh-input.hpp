#pragma once // #

#include "./config.hpp"
#include "./driver.hpp"
#include "./thread.hpp"
#include "./context.hpp"
#include "./bview-set.hpp"

namespace jvi {

    // WARNING!!
    // FOR VULKAN API ONLY!!
    // PLANNED: OPENGL VERSION!!
    class MeshInput : public std::enable_shared_from_this<MeshInput> {
    public: friend Node; friend Renderer; friend MeshBinding;
        MeshInput() {};
        MeshInput(const vkt::uni_ptr<Context>& context) : context(context) { this->construct(); };
        MeshInput(const std::shared_ptr<Context>& context) : context(context) { this->construct(); };
        ~MeshInput() {};

        // 
        virtual vkt::uni_ptr<MeshInput> sharedPtr() { return shared_from_this(); };
        //virtual vkt::uni_ptr<MeshInput> sharedPtr() const { return std::shared_ptr<MeshInput>(shared_from_this()); };

        //
        virtual uPTR(MeshInput) makeQuad(const bool& quad = true) { this->needsQuads = quad; return uTHIS; };

        // 
        virtual uPTR(MeshInput) construct() {
            this->driver = context->getDriver();
            this->thread = std::make_shared<Thread>(this->driver);

            // 
            this->pipelineInfo = vkh::VsGraphicsPipelineCreateInfoConstruction();

            // 
            this->quadStage = vkt::makePipelineStageInfo(this->driver->getDevice(), vkt::readBinary("./shaders/rtrace/quad.comp.spv"), vk::ShaderStageFlagBits::eCompute);

            // for faster code, pre-initialize
            this->stages = vkt::vector_cast<vkh::VkPipelineShaderStageCreateInfo, vk::PipelineShaderStageCreateInfo>({
                vkt::makePipelineStageInfo(this->driver->getDevice(), vkt::readBinary("./shaders/rtrace/transform.vert.spv"), vk::ShaderStageFlagBits::eVertex),
                vkt::makePipelineStageInfo(this->driver->getDevice(), vkt::readBinary("./shaders/rtrace/transform.geom.spv"), vk::ShaderStageFlagBits::eGeometry)
            });

            // transformPipelineLayout
            // create required buffers
            this->rawBindings = vkt::Vector<VkVertexInputBindingDescription>(std::make_shared<vkt::VmaBufferAllocation>(this->driver->getAllocator(), vkh::VkBufferCreateInfo{ .size = sizeof(VkVertexInputBindingDescription) * 8u, .usage = {.eTransferSrc = 1, .eUniformBuffer = 1, .eStorageBuffer = 1, .eRayTracing = 1 } }, VMA_MEMORY_USAGE_CPU_TO_GPU));
            this->gpuBindings = vkt::Vector<VkVertexInputBindingDescription>(std::make_shared<vkt::VmaBufferAllocation>(this->driver->getAllocator(), vkh::VkBufferCreateInfo{ .size = sizeof(VkVertexInputBindingDescription) * 8u, .usage = {.eTransferDst = 1, .eUniformBuffer = 1, .eStorageBuffer = 1, .eRayTracing = 1 } }, VMA_MEMORY_USAGE_GPU_ONLY));
            this->rawAttributes = vkt::Vector<VkVertexInputAttributeDescription>(std::make_shared<vkt::VmaBufferAllocation>(this->driver->getAllocator(), vkh::VkBufferCreateInfo{ .size = sizeof(VkVertexInputAttributeDescription) * 8u, .usage = {.eTransferSrc = 1, .eUniformBuffer = 1, .eStorageBuffer = 1, .eRayTracing = 1 } }, VMA_MEMORY_USAGE_CPU_TO_GPU));
            this->gpuAttributes = vkt::Vector<VkVertexInputAttributeDescription>(std::make_shared<vkt::VmaBufferAllocation>(this->driver->getAllocator(), vkh::VkBufferCreateInfo{ .size = sizeof(VkVertexInputAttributeDescription) * 8u, .usage = {.eTransferDst = 1, .eUniformBuffer = 1, .eStorageBuffer = 1, .eRayTracing = 1 } }, VMA_MEMORY_USAGE_GPU_ONLY));

            // 
            return uTHIS;
        };

        // 
        virtual uPTR(MeshInput) createDescriptorSet() { // 
            if (this->descriptorSetInitialized) {
                return uTHIS; // TODO: Optional Un-Protect
            };

            // 
            this->descriptorSetHelper = vkh::VsDescriptorSetCreateInfoHelper(this->transformSetLayout[0], this->driver->getDescriptorPool());

            // 
            this->descriptorSetHelper.pushDescription(vkh::VkDescriptorUpdateTemplateEntry{
                .dstBinding = 0u,
                .dstArrayElement = 0u,
                .descriptorCount = 1u,
                .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER
                }).offset<vk::DescriptorBufferInfo>() = gpuBindings;

            // 
            this->descriptorSetHelper.pushDescription(vkh::VkDescriptorUpdateTemplateEntry{
                .dstBinding = 1u,
                .dstArrayElement = 0u,
                .descriptorCount = 1u,
                .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER
                }).offset<vk::DescriptorBufferInfo>() = gpuAttributes;

            // 
            driver->getDevice().updateDescriptorSets(vkt::vector_cast<vk::WriteDescriptorSet, vkh::VkWriteDescriptorSet>(
                this->descriptorSetHelper.setDescriptorSet((this->descriptorSet = this->descriptorSet.size() > 0 && this->descriptorSet[0u] ? this->descriptorSet : driver->getDevice().allocateDescriptorSets(this->descriptorSetHelper))[0])
            ), {});

            // 
            if (this->bvs) { this->bvs->createDescriptorSet(); };
            this->descriptorSetInitialized = true;

            // 
            return uTHIS;
        };

        // 
        virtual uPTR(MeshInput) copyMeta(vk::CommandBuffer buildCommand = {}) {
            buildCommand.copyBuffer(this->rawAttributes, this->gpuAttributes, { vk::BufferCopy{ this->rawAttributes.offset(), this->gpuAttributes.offset(), this->gpuAttributes.range() } });
            buildCommand.copyBuffer(this->rawBindings, this->gpuBindings, { vk::BufferCopy{ this->rawBindings.offset(), this->gpuBindings.offset(), this->gpuBindings.range() } });
            return uTHIS;
        };

        // 
        virtual uPTR(MeshInput) formatQuads(const std::shared_ptr<jvi::MeshBinding>& binding, vkt::uni_arg<glm::u64vec4> offsetHelp, vkt::uni_arg<VkCommandBuffer> buildCommand = {}) {
            return this->formatQuads(binding, offsetHelp, buildCommand);
        };

        //
        virtual uPTR(MeshInput) formatQuads(const vkt::uni_ptr<jvi::MeshBinding>& binding, vkt::uni_arg<glm::u64vec4> offsetHelp, vkt::uni_arg<VkCommandBuffer> buildCommand = {}) {
            return this->formatQuads(binding, offsetHelp, vk::CommandBuffer(*buildCommand));
        };

        // 
        virtual uPTR(MeshInput) formatQuads(const vkt::uni_ptr<jvi::MeshBinding>& binding, vkt::uni_arg<glm::u64vec4> offsetHelp, vkt::uni_arg<vk::CommandBuffer> buildCommand = {}) { // 
            bool DirectCommand = false, HasCommand = buildCommand.has() && buildCommand && *buildCommand;

            // Initialize Input (Early)
            if (this->needsQuads) { this->createRasterizePipeline()->createDescriptorSet(); };

            // 
            if (!HasCommand || ignoreIndirection) {
                buildCommand = vkt::createCommandBuffer(this->thread->getDevice(), this->thread->getCommandPool()); DirectCommand = true;
            };

            // 
            if (this->indexData) {
                this->meta.indexID = *this->indexData;
                this->meta.indexType = int32_t(this->indexType) + 1;
            };

            // 
            if (this->bvs) {
                this->descriptorSet.resize(2u);
                this->descriptorSet[1u] = this->bvs->getDescriptorSet();
            };

            // NO! Please, re-make QUAD internally! (P.S. MeshBinding)
            if (HasCommand && this->needsQuads) { // TODO: scratch buffer
                this->needsQuads = false; // FOR MINECRAFT ONLY! 
                this->quadInfo.layout = this->transformPipelineLayout;
                this->quadInfo.stage = this->quadStage;
                if (!this->quadGenerator) {
                    this->quadGenerator = vkt::handleHpp(vkt::createCompute(driver->getDevice(), vkt::FixConstruction(this->quadStage), vk::PipelineLayout(this->quadInfo.layout), driver->getPipelineCache()));
                };

                // 
                const auto originalCt = this->currentUnitCount;
                const uint32_t ucount = vkt::tiled(originalCt, 1024ull);

                // 
                buildCommand->bindDescriptorSets(vk::PipelineBindPoint::eCompute, this->transformPipelineLayout, 0ull, this->descriptorSet, {});
                buildCommand->bindPipeline(vk::PipelineBindPoint::eCompute, this->quadGenerator);
                buildCommand->pushConstants<jvi::MeshInfo>(this->transformPipelineLayout, vkh::VkShaderStageFlags{.eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 }.hpp(), 0u, { meta });
                buildCommand->dispatch(ucount, 1u, 1u);
                vkt::commandBarrier(buildCommand);

                // Now should to be triangles!
                if (this->indexData) {
                    this->setIndexData(this->meta.indexID, this->indexType)->manifestIndex(vk::IndexType::eUint32)->setIndexCount(vkt::tiled(originalCt, 4ull) * 6u);
                };
            };

            if (DirectCommand) {
                vkt::submitCmd(this->thread->getDevice(), this->thread->getQueue(), { buildCommand });
                this->thread->getDevice().freeCommandBuffers(this->thread->getCommandPool(), { buildCommand });
            };

            return uTHIS;
        };

        // 
        virtual uPTR(MeshInput) buildGeometry(const std::shared_ptr<jvi::MeshBinding>& binding, vkt::uni_arg<glm::u64vec4> offsetHelp, vkt::uni_arg<VkCommandBuffer> buildCommand = {}) {
            return this->buildGeometry(binding, offsetHelp, buildCommand);
        };

        //
        virtual uPTR(MeshInput) buildGeometry(const vkt::uni_ptr<jvi::MeshBinding>& binding, vkt::uni_arg<glm::u64vec4> offsetHelp, vkt::uni_arg<VkCommandBuffer> buildCommand = {}){
            return this->buildGeometry(binding, offsetHelp, vk::CommandBuffer(*buildCommand));
        };

        // Record Geometry (Transform Feedback), Re-Implemented in another file... 
        virtual uPTR(MeshInput) buildGeometry(const vkt::uni_ptr<jvi::MeshBinding>& binding, vkt::uni_arg<glm::u64vec4> offsetHelp, vkt::uni_arg<vk::CommandBuffer> buildCommand = {});

        // 
        template<class T = uint8_t>
        inline uPTR(MeshInput) addBinding(const uint32_t& rawData, vkt::uni_arg<vkh::VkVertexInputBindingDescription> binding = vkh::VkVertexInputBindingDescription{}) {
            const uintptr_t bindingID = this->vertexInputBindingDescriptions.size();
            this->vertexInputBindingDescriptions.resize(bindingID + 1u);
            this->vertexInputBindingDescriptions[bindingID] = binding;
            this->vertexInputBindingDescriptions[bindingID].binding = static_cast<uint32_t>(bindingID);
            this->rawBindings[bindingID] = this->vertexInputBindingDescriptions[bindingID];
            this->rawBindings[bindingID].binding = rawData;

            // 
            this->bindRange.resize(bindingID + 1u);
            this->bindRange[this->lastBindID = static_cast<uint32_t>(bindingID)] = this->bvs->get(rawData).range();
            this->bindings.resize(bindingID+1u);
            this->bindings[bindingID] = rawData;
            return uTHIS;
        };

        // 
        virtual uPTR(MeshInput) manifestIndex(const vk::IndexType& type = vk::IndexType::eNoneKHR) {
            if (this->rawMeshInfo.has()) {
                this->rawMeshInfo[0u].indexType = uint32_t(this->indexType = type) + 1u;
            } else {
                this->indexType = type;
            }
            return uTHIS;
        };

        // 
        virtual uPTR(MeshInput) addAttribute(vkt::uni_arg<vkh::VkVertexInputAttributeDescription> attribute = vkh::VkVertexInputAttributeDescription{}, const bool& NotStub = true) {
            const uintptr_t bindingID = this->lastBindID;//attribute->binding;
            const uintptr_t locationID = attribute->location;
            this->vertexInputAttributeDescriptions.resize(locationID + 1u);
            this->vertexInputAttributeDescriptions[locationID] = attribute;
            this->vertexInputAttributeDescriptions[locationID].binding = static_cast<uint32_t>(bindingID);
            this->vertexInputAttributeDescriptions[locationID].location = static_cast<uint32_t>(locationID);
            this->rawAttributes[locationID] = this->vertexInputAttributeDescriptions[locationID];

            // 
            if (locationID == 0u && NotStub && this->indexType == vk::IndexType::eNoneKHR) {
                this->currentUnitCount = this->bindRange[bindingID] / this->bvs->get(this->bindings[bindingID]).stride();
            };

            // 
            if (rawMeshInfo.has()) {
                if (locationID == 1u && NotStub) { rawMeshInfo[0].hasTexcoord = meta.hasTexcoord = 1; };
                if (locationID == 2u && NotStub) { rawMeshInfo[0].hasNormal = meta.hasNormal = 1; };
                if (locationID == 3u && NotStub) { rawMeshInfo[0].hasTangent = meta.hasTangent = 1; };

                if (locationID == 0u && NotStub && this->needsQuads) { // PIDORS IN MICROSOFT!
                    this->rawMeshInfo[0u].indexType = uint32_t(this->indexType = vk::IndexType::eUint32) + 1u;
                };
            } else {
                if (locationID == 1u && NotStub) { meta.hasTexcoord = 1; };
                if (locationID == 2u && NotStub) { meta.hasNormal = 1; };
                if (locationID == 3u && NotStub) { meta.hasTangent = 1; };
            };

            // 
            return uTHIS;
        };

        // 
        virtual uPTR(MeshInput) setIndexOffset(const vk::DeviceSize& offset = 0ull) { this->indexOffset = offset; return uTHIS; };
        virtual uPTR(MeshInput) setIndexCount(const vk::DeviceSize& count = 65536u * 3u) { this->currentUnitCount = count; return uTHIS; };
        virtual uPTR(MeshInput) setPrimitiveCount(const vk::DeviceSize& count = 65536u) { this->setIndexCount(count * 3u); return uTHIS; };

        // 
        virtual const vk::DeviceSize& getIndexCount() const { return this->currentUnitCount; };
        virtual vk::DeviceSize& getIndexCount() { return this->currentUnitCount; };

        // 
        template<class T = uint8_t>
        inline uPTR(MeshInput) setIndexData(const uint32_t& rawIndices, const vk::IndexType& type) {
        //inline uPTR(MeshInput) setIndexData(const vkt::Vector<T>& rawIndices, const vk::IndexType& type) {
            vk::DeviceSize count = 0u; uint32_t stride = 1u;
            //if (rawIndices) {
                switch (type) { // 
                    case vk::IndexType::eUint32:   count = this->bvs->get(rawIndices)->range() / (stride = 4u); break;
                    case vk::IndexType::eUint16:   count = this->bvs->get(rawIndices)->range() / (stride = 2u); break;
                    case vk::IndexType::eUint8EXT: count = this->bvs->get(rawIndices)->range() / (stride = 1u); break;
                    default: count = 0u;
                };
            //};
            // 
            this->indexData = rawIndices; // 
            this->indexType = (this->indexData && type != vk::IndexType::eNoneKHR) ? type : vk::IndexType::eNoneKHR;

            // 
            if (this->indexType != vk::IndexType::eNoneKHR) {
                this->currentUnitCount = this->bvs->get(rawIndices).range() / stride;
            };

            return uTHIS;
        };

        // 
        //virtual uPTR(MeshInput) setIndexData(const vkt::Vector<uint32_t>& rawIndices) { return this->setIndexData(rawIndices, vk::IndexType::eUint32); };
        //virtual uPTR(MeshInput) setIndexData(const vkt::Vector<uint16_t>& rawIndices) { return this->setIndexData(rawIndices, vk::IndexType::eUint16); };
        //virtual uPTR(MeshInput) setIndexData(const vkt::Vector<uint8_t >& rawIndices) { return this->setIndexData(rawIndices, vk::IndexType::eUint8EXT); };
        virtual uPTR(MeshInput) setIndexData() { return this->setIndexData({}, vk::IndexType::eNoneKHR); };

        // some type dependent
        //template<class T = uint8_t>
        //inline uPTR(MeshInput) setIndexData(const vkt::Vector<T>& rawIndices = {}) { return this->setIndexData(rawIndices); };

        // 
        virtual uPTR(MeshInput) createRasterizePipeline() {
            if (this->transformState) {
                return uTHIS; // TODO: Optional Un-Protect
            };

            const auto& viewport = this->context->refViewport();
            const auto& renderArea = this->context->refScissor();
            const auto& TFI = vk::PipelineRasterizationStateStreamCreateInfoEXT().setRasterizationStream(0u);

            // 
            //this->transformSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 0u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_TEXEL_BUFFER, .descriptorCount = 16u, .stageFlags = {.eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1 });
            this->transformSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 0u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER, .descriptorCount = 1u, .stageFlags = {.eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1 } });
            this->transformSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 1u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER, .descriptorCount = 1u, .stageFlags = {.eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1 } });
            this->transformSetLayout[0] = driver->getDevice().createDescriptorSetLayout(transformSetLayoutHelper);

            // 
            if (this->bvs) {
                this->transformSetLayout.resize(2u);
                this->transformSetLayout[1] = this->bvs->getDescriptorLayout();
            };

            // 
            std::vector<vkh::VkPushConstantRange> ranges = { {.stageFlags = {.eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 }, .offset = 0u, .size = 16u } };
            this->transformPipelineLayout = driver->getDevice().createPipelineLayout(vkh::VkPipelineLayoutCreateInfo{}.setSetLayouts(transformSetLayout).setPushConstantRanges(ranges));

            // 
            this->pipelineInfo.rasterizationState.pNext = &TFI;
            this->pipelineInfo.rasterizationState.rasterizerDiscardEnable = true;

            // 
            this->pipelineInfo = vkh::VsGraphicsPipelineCreateInfoConstruction();
            this->pipelineInfo.vertexInputAttributeDescriptions = this->vertexInputAttributeDescriptions;
            this->pipelineInfo.vertexInputBindingDescriptions = this->vertexInputBindingDescriptions;
            this->pipelineInfo.stages = this->stages;
            //this->pipelineInfo.depthStencilState = vkh::VkPipelineDepthStencilStateCreateInfo{ .depthTestEnable = true, .depthWriteEnable = true };
            this->pipelineInfo.dynamicStates.push_back(VK_DYNAMIC_STATE_VIEWPORT);
            this->pipelineInfo.dynamicStates.push_back(VK_DYNAMIC_STATE_SCISSOR);
            this->pipelineInfo.graphicsPipelineCreateInfo.renderPass = this->context->renderPass;
            this->pipelineInfo.graphicsPipelineCreateInfo.layout = this->transformPipelineLayout;

            // 
            for (uint32_t i = 0u; i < 8u; i++) {
                this->pipelineInfo.colorBlendAttachmentStates.push_back(vkh::VkPipelineColorBlendAttachmentState{ .blendEnable = true }); // transparency will generated by ray-tracing
            };

            // 
            this->transformState = vkt::handleHpp(driver->getDevice().createGraphicsPipeline(driver->getPipelineCache(), this->pipelineInfo));

            // 
            return uTHIS;
        };

        // 
        virtual uPTR(MeshInput) linkCounterBuffer(const vkt::Vector<vkh::VkAccelerationStructureBuildOffsetInfoKHR>& offsetInfo = {}) {
            //this->counterData = offsetInfo; // DANGER!! BSOD BY LOCKING GPU!
            return uTHIS;
        };

        // 
        virtual uPTR(MeshInput) linkBViewSet(const std::shared_ptr<BufferViewSet>& bufferViewSet = {}) {
            return this->linkBViewSet(vkt::uni_ptr<BufferViewSet>(bufferViewSet));
        };

        // 
        virtual uPTR(MeshInput) linkBViewSet(const vkt::uni_ptr<BufferViewSet>& bufferViewSet = {}) {
            this->bvs = bufferViewSet;
            return uTHIS;
        };

    protected: friend Node; friend Renderer; // Partitions
        uint64_t checkoutPointData = 0ull;
        MeshInfo meta = {};

        // 
        vk::PipelineLayout transformPipelineLayout = {};
        std::vector<vk::DescriptorSet> descriptorSet = { {}, {} };
        std::vector<VkDescriptorSetLayout> transformSetLayout = { {}, {} };
        vkh::VsDescriptorSetLayoutCreateInfoHelper transformSetLayoutHelper = {};
        vkh::VsDescriptorSetCreateInfoHelper descriptorSetHelper = {};

        // 
        std::vector<vkh::VkVertexInputBindingDescription> vertexInputBindingDescriptions = {};
        vkt::Vector<vkh::VkVertexInputBindingDescription> rawBindings = {}, gpuBindings = {};

        // 
        std::vector<vkh::VkVertexInputAttributeDescription> vertexInputAttributeDescriptions = {};
        vkt::Vector<vkh::VkVertexInputAttributeDescription> rawAttributes = {}, gpuAttributes = {};

        // 
        std::vector<uint32_t> bindings = {}, bindRange = { 0 };
        vkt::Vector<MeshInfo> rawMeshInfo = { }; // BROKEN?!
        vk::IndexType indexType = vk::IndexType::eNoneKHR;

        // 
        vk::DeviceSize indexOffset = 0ull;
        std::optional<uint32_t> indexData;

        // 
        vkt::uni_ptr<BufferViewSet> bvs = {};
        uint32_t lastBindID = 0u;

        // 
        vk::DeviceSize currentUnitCount = 0u;
        bool needsQuads = false, needUpdate = true, ignoreIndirection = false;
        bool descriptorSetInitialized = false;

        // 
        std::vector<vkh::VkPipelineShaderStageCreateInfo> stages = {};
        vkh::VsGraphicsPipelineCreateInfoConstruction pipelineInfo = {};
        //vkh::VkAccelerationStructureBuildOffsetInfoKHR offsetMeta = {};

        // 
        vkh::VkComputePipelineCreateInfo quadInfo = {};
        vkh::VkPipelineShaderStageCreateInfo quadStage = {};
        vk::Pipeline quadGenerator = {}, transformState = {};

        // 
        vkt::uni_ptr<Driver> driver = {};
        vkt::uni_ptr<Thread> thread = {};
        vkt::uni_ptr<Context> context = {};
        //vkt::uni_ptr<Renderer> renderer = {};
    };
};
