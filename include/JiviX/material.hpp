#pragma once // #
#include "./config.hpp"
#include "./driver.hpp"
#include "./thread.hpp"
#include "./context.hpp"

namespace jvx {

    // 
#pragma pack(push, 1)
    struct MaterialUnit {
        glm::vec4 diffuse = { 1.f,1.f,1.f,1.f };
        glm::vec4 specular = { 0.f,0.f,0.f,1.f };
        glm::vec4 normals = { 0.5f,0.5f,1.f,1.f };
        glm::vec4 emission = { 0.0f,0.0f,0.f,1.f };

        int diffuseTexture = -1;
        int specularTexture = -1;
        int normalsTexture = -1;
        int emissionTexture = -1;

        glm::uvec4 udata = glm::uvec4(0u);
    };
#pragma pack(pop)

    // WIP Materials
    class Material : public std::enable_shared_from_this<Material> { public: 
        Material(const std::shared_ptr<Context>& context) {
            this->driver = context->getDriver();
            this->thread = std::make_shared<Thread>(this->driver);
            this->context = context;

            // 
            this->rawMaterials = vkt::Vector<vkh::VsGeometryInstance>(std::make_shared<vkt::VmaBufferAllocation>(this->driver->getAllocator(), vkh::VkBufferCreateInfo{ .size = sizeof(MaterialUnit)*64u, .usage = { .eTransferSrc = 1, .eUniformBuffer = 1, .eStorageBuffer = 1, .eRayTracing = 1 } }, VMA_MEMORY_USAGE_CPU_TO_GPU));
            this->gpuMaterials = vkt::Vector<vkh::VsGeometryInstance>(std::make_shared<vkt::VmaBufferAllocation>(this->driver->getAllocator(), vkh::VkBufferCreateInfo{ .size = sizeof(MaterialUnit)*64u, .usage = { .eTransferDst = 1, .eUniformBuffer = 1, .eStorageBuffer = 1, .eRayTracing = 1 } }, VMA_MEMORY_USAGE_GPU_ONLY));
        };

        // 
        std::shared_ptr<Material> setContext(const std::shared_ptr<Context>& context) {
            this->context = context;
            this->descriptorSetInfo = vkh::VsDescriptorSetCreateInfoHelper(this->context->materialDescriptorSetLayout,this->thread->getDescriptorPool());
            return shared_from_this();
        };

        // 
        std::shared_ptr<Material> setRawMaterials(const vkt::Vector<MaterialUnit>& rawMaterials = {}, const vk::DeviceSize& materialCounter = 0u) {
            this->rawMaterials = rawMaterials; this->materialCounter = materialCounter;
            return shared_from_this();
        };

        // 
        std::shared_ptr<Material> setGpuMaterials(const vkt::Vector<MaterialUnit>& gpuMaterials = {}) {
            this->gpuMaterials = gpuMaterials;
            return shared_from_this();
        };

        // 
        std::shared_ptr<Material> pushMaterial(const MaterialUnit& material = {}) {
            const auto materialID = materialCounter++;
            this->rawMaterials[materialID] = material;
            return shared_from_this();
        };

        // 
        std::shared_ptr<Material> pushSampledImage(const vkh::VkDescriptorImageInfo& info = {}) {
            this->sampledImages.push_back(info);
            return shared_from_this();
        };

        // 
        std::shared_ptr<Material> copyBuffers(const vk::CommandBuffer& buildCommand = {}) {
            buildCommand.copyBuffer(this->rawMaterials, this->gpuMaterials, { vk::BufferCopy{ this->rawMaterials.offset(), this->gpuMaterials.offset(), this->gpuMaterials.range() } });
            return shared_from_this();
        };

        // 
        std::shared_ptr<Material> createDescriptorSet() {
            this->descriptorSetInfo = vkh::VsDescriptorSetCreateInfoHelper(context->materialDescriptorSetLayout, driver->descriptorPool);

            if (sampledImages.size() > 0u) { // Setup Textures
                vkh::VsDescriptorHandle<VkDescriptorImageInfo> imagesHandle = this->descriptorSetInfo.pushDescription(vkh::VkDescriptorUpdateTemplateEntry{
                    .dstBinding = 0u,
                    .descriptorCount = uint32_t(sampledImages.size()),
                    .descriptorType = VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER
                });
                memcpy(&imagesHandle.offset<VkDescriptorImageInfo>(), sampledImages.data(), sampledImages.size() * sizeof(VkDescriptorImageInfo));
                //for (uint32_t i = 0u; i < sampledImages.size(); i++) { imagesHandle.offset<VkDescriptorImageInfo>(i) = sampledImages[i]; };
            };

            // 
            this->descriptorSetInfo.pushDescription(vkh::VkDescriptorUpdateTemplateEntry{
                .dstBinding = 1u,
                .descriptorCount = 1u,
                .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER
            }).offset<vkh::VkDescriptorBufferInfo>() = (vkh::VkDescriptorBufferInfo&)this->gpuMaterials;

            // Reprojection WILL NOT write own depth... 
            this->context->descriptorSets[4] = this->descriptorSet = driver->getDevice().allocateDescriptorSets(this->descriptorSetInfo)[0];
            this->driver->getDevice().updateDescriptorSets(vkt::vector_cast<vk::WriteDescriptorSet, vkh::VkWriteDescriptorSet>(this->descriptorSetInfo.setDescriptorSet(this->descriptorSet)), {});
            return shared_from_this();
        };

    protected: // 
        std::vector<vkh::VkDescriptorImageInfo> sampledImages = {};

        // 
        vkt::Vector<MaterialUnit> rawMaterials = {}; // Ray-Tracing instances Will re-located into meshes by Index, and will no depending by mesh list...
        vkt::Vector<MaterialUnit> gpuMaterials = {};

        // 
        vkh::VsDescriptorSetCreateInfoHelper descriptorSetInfo = {};
        vk::DescriptorSet descriptorSet = {};

        // 
        vk::DeviceSize materialCounter = 0u;
        std::shared_ptr<Driver> driver = {};
        std::shared_ptr<Thread> thread = {};
        std::shared_ptr<Context> context = {};
    };
};
