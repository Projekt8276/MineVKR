#pragma once // #

#include "./config.hpp"
#include "./driver.hpp"
#include "./thread.hpp"

namespace jvi {

#pragma pack(push, 1)
    struct Matrices {
        glm::mat4 projection = glm::mat4(1.f);
        glm::mat4 projectionInv = glm::mat4(1.f);
        glm::mat3x4 modelview = glm::mat3x4(1.f);
        glm::mat3x4 modelviewInv = glm::mat3x4(1.f);
        glm::mat3x4 modelviewPrev = glm::mat3x4(1.f);
        glm::mat3x4 modelviewPrevInv = glm::mat3x4(1.f);
        glm::uvec4 mdata = glm::uvec4(0u);                         // mesh mutation or modification data
        //glm::uvec2 tdata = glm::uvec2(0u), rdata = glm::uvec2(0u); // first for time, second for randoms
        glm::uvec2 tdata = glm::uvec2(0u);
        glm::uvec2 rdata = glm::uvec2(0u);
    };
#pragma pack(pop)

    // 
    class Context : public std::enable_shared_from_this<Context> { public: friend MeshInput; friend MeshBinding; friend Node; friend Driver; friend Material; friend Renderer;
        Context() {};
        Context(const vkt::uni_ptr<Driver>& driver) : driver(driver) { this->construct(); };
        Context(const std::shared_ptr<Driver>& driver) : driver(driver) { this->construct(); };
        ~Context() {};

        // 
        virtual vkt::uni_ptr<Context> sharedPtr() { return shared_from_this(); };
        //virtual vkt::uni_ptr<Context> sharedPtr() const { return shared_from_this(); };

        // 
        virtual uPTR(Context) construct() {
            this->thread = std::make_shared<Thread>(this->driver);
            this->uniformGPUData = vkt::Vector<jvi::Matrices>(std::make_shared<vkt::VmaBufferAllocation>(this->driver->getAllocator(), vkh::VkBufferCreateInfo{ .size = sizeof(Matrices) * 2u, .usage = {.eTransferSrc = 1, .eTransferDst = 1, .eUniformBuffer = 1, .eStorageBuffer = 1, .eRayTracing = 1 } }, VMA_MEMORY_USAGE_GPU_ONLY));
            this->uniformRawData = vkt::Vector<jvi::Matrices>(std::make_shared<vkt::VmaBufferAllocation>(this->driver->getAllocator(), vkh::VkBufferCreateInfo{ .size = sizeof(Matrices) * 2u, .usage = {.eTransferSrc = 1,                    .eUniformBuffer = 1, .eStorageBuffer = 1, .eRayTracing = 1 } }, VMA_MEMORY_USAGE_CPU_TO_GPU));
            this->beginTime = std::chrono::high_resolution_clock::now();
            this->leastTime = std::chrono::high_resolution_clock::now();
            this->previTime = std::chrono::high_resolution_clock::now();

            // 
            glm::mat4x4 projected = glm::perspective(80.f / 180.f * glm::pi<float>(), float(1600) / float(1200), 0.001f, 10000.f);
            glm::mat4x4 modelview = glm::lookAt(glm::vec3(0.f, 0.f, 1.f), glm::vec3(0.f, 0.f, 0.f), glm::vec3(0.f, 1.f, 0.f));

            // 
            uniformRawData[0].modelview = glm::transpose(modelview);
            uniformRawData[0].modelviewInv = glm::transpose(glm::inverse(modelview));
            uniformRawData[0].projection = glm::transpose(projected);
            uniformRawData[0].projectionInv = glm::transpose(glm::inverse(projected));
            return uTHIS;
        };

        // 
        virtual uPTR(Context) setThread(const vkt::uni_ptr<Thread>& thread) {
            this->thread = thread;
            return uTHIS;
        };

        virtual vkt::uni_ptr<Thread>& getThread() {
            return this->thread;
        };

        virtual const vkt::uni_ptr<Thread>& getThread() const {
            return this->thread;
        };

        // 
        virtual vk::Rect2D& refScissor() { return scissor; };
        virtual vk::Viewport& refViewport() { return viewport; };
        virtual vk::RenderPass& refRenderPass() { return renderPass; };
        //vk::Framebuffer& refFramebuffer() { return framebuffer; };

        // 
        virtual const vk::Rect2D& refScissor() const { return scissor; };
        virtual const vk::Viewport& refViewport() const { return viewport; };
        virtual const vk::RenderPass& refRenderPass() const { return renderPass; };
        //const vk::Framebuffer& refFramebuffer() const { return framebuffer; };

        // 
        virtual vkt::uni_ptr<Driver>& getDriver() { return driver; };
        virtual const vkt::uni_ptr<Driver>& getDriver() const { return driver; };

        // 
        virtual uPTR(Context) createRenderPass() { // 
            std::cout << "Create Render Pass" << std::endl;

            vkh::VsRenderPassCreateInfoHelper rpsInfo = {};

            for (uint32_t b=0u;b<8u;b++) {
                rpsInfo.addColorAttachment(vkh::VkAttachmentDescription{
                    .format = VK_FORMAT_R32G32B32A32_SFLOAT,
                    .loadOp = VK_ATTACHMENT_LOAD_OP_LOAD,
                    .storeOp = VK_ATTACHMENT_STORE_OP_STORE,
                    .stencilLoadOp = VK_ATTACHMENT_LOAD_OP_DONT_CARE,
                    .stencilStoreOp = VK_ATTACHMENT_STORE_OP_DONT_CARE,
                    .initialLayout = VK_IMAGE_LAYOUT_UNDEFINED,
                    .finalLayout = VK_IMAGE_LAYOUT_GENERAL,
                });
            };

            // 
            rpsInfo.setDepthStencilAttachment(vkh::VkAttachmentDescription{
                .format = VK_FORMAT_D32_SFLOAT_S8_UINT,
                .loadOp = VK_ATTACHMENT_LOAD_OP_LOAD,
                .storeOp = VK_ATTACHMENT_STORE_OP_STORE,
                .stencilLoadOp = VK_ATTACHMENT_LOAD_OP_DONT_CARE,
                .stencilStoreOp = VK_ATTACHMENT_STORE_OP_DONT_CARE,
                .initialLayout = VK_IMAGE_LAYOUT_UNDEFINED,
                .finalLayout = VK_IMAGE_LAYOUT_GENERAL//VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL,
            });

            // 
            rpsInfo.addSubpassDependency(vkh::VkSubpassDependency{
                .srcSubpass = VK_SUBPASS_EXTERNAL,
                .dstSubpass = 0u,
                .srcStageMask = {.eColorAttachmentOutput = 1, .eTransfer = 1, .eBottomOfPipe = 1},
                .dstStageMask = {.eColorAttachmentOutput = 1},
                .srcAccessMask = {.eColorAttachmentWrite = 1},
                .dstAccessMask = {.eColorAttachmentRead = 1, .eColorAttachmentWrite = 1},
                .dependencyFlags = {.eByRegion = 1}
            });

            // 
            rpsInfo.addSubpassDependency(vkh::VkSubpassDependency{
                .srcSubpass = 0u,
                .dstSubpass = VK_SUBPASS_EXTERNAL,
                .srcStageMask = {.eColorAttachmentOutput = 1},
                .dstStageMask = {.eTopOfPipe = 1, .eColorAttachmentOutput = 1, .eTransfer = 1},
                .srcAccessMask = {.eColorAttachmentRead = 1, .eColorAttachmentWrite = 1},
                .dstAccessMask = {.eColorAttachmentRead = 1, .eColorAttachmentWrite = 1},
                .dependencyFlags = {.eByRegion = 1}
            });

            // 
            this->renderPass = driver->getDevice().createRenderPass(rpsInfo);

            // 
            return uTHIS;
        };

        // 
        virtual std::array<vk::DescriptorSet,5u>& getDescriptorSets() {
            return descriptorSets;
        };

        // 
        virtual vk::PipelineLayout getPipelineLayout() {
            return unifiedPipelineLayout;
        };

        // 
        virtual uPTR(Context) registerTime() {
            this->previTime = this->leastTime;
            uniformRawData[0].tdata[0] = std::chrono::duration_cast<std::chrono::milliseconds>((this->leastTime = std::chrono::high_resolution_clock::now()) - this->beginTime).count(); // time from beginning
            uniformRawData[0].tdata[1] = std::chrono::duration_cast<std::chrono::milliseconds>(this->leastTime - this->previTime).count(); // difference 
            return uTHIS;
        };

        // 
        virtual uint32_t& drawTime() { return uniformRawData[0].tdata[0u]; };
        virtual uint32_t& timeDiff() { return uniformRawData[0].tdata[1u]; };
        virtual const uint32_t& drawTime() const { return uniformRawData[0].tdata[0u]; };
        virtual const uint32_t& timeDiff() const { return uniformRawData[0].tdata[1u]; };

        // 
        virtual uPTR(Context) setDrawCount(const uint32_t& count = 0u) {
            uniformRawData[0].rdata[0] = count;
            return uTHIS;
        };

        // 
        virtual uPTR(Context) setPerspective(const glm::mat4x4& persp = glm::mat4(1.f)) {
            uniformRawData[0].projection = glm::transpose(persp);
            uniformRawData[0].projectionInv = glm::transpose(glm::inverse(persp));
            return uTHIS;
        };

        // 
        virtual uPTR(Context) setModelView(const glm::mat4x4& mv = glm::mat3x4(1.f)) {
            uniformRawData[0].modelview = glm::transpose(mv);
            uniformRawData[0].modelviewInv = glm::transpose(glm::inverse(mv));
            return uTHIS;
        };


        // 
        vkt::ImageRegion& getFrameBuffer(const uint32_t& I = 0u) { return this->frameBfImages[I]; };
        const vkt::ImageRegion& getFrameBuffer(const uint32_t& I = 0u) const { return this->frameBfImages[I]; };

        // 
        vkt::ImageRegion& getFlip0Buffer(const uint32_t& I = 0u) { return this->smFlip0Images[I]; };
        const vkt::ImageRegion& getFlip0Buffer(const uint32_t& I = 0u) const { return this->smFlip0Images[I]; };

        // 
        vkt::ImageRegion& getFlip1Buffer(const uint32_t& I = 0u) { return this->smFlip1Images[I]; };
        const vkt::ImageRegion& getFlip1Buffer(const uint32_t& I = 0u) const { return this->smFlip1Images[I]; };


        // 
        virtual std::array<vkt::ImageRegion, 12u>& getFlip0Buffers() {
            return this->smFlip0Images;
        }

        // 
        virtual const std::array<vkt::ImageRegion, 12u>& getFlip0Buffers() const {
            return this->smFlip0Images;
        }

        // 
        virtual std::array<vkt::ImageRegion, 12u>& getFlip1Buffers() {
            return this->smFlip1Images;
        }

        // 
        virtual const std::array<vkt::ImageRegion, 12u>& getFlip1Buffers() const {
            return this->smFlip1Images;
        }

        // 
        virtual std::array<vkt::ImageRegion, 12u>& getFrameBuffers() {
            return this->frameBfImages;
        }

        // 
        virtual const std::array<vkt::ImageRegion, 12u>& getFrameBuffers() const {
            return this->frameBfImages;
        }

        // 
        virtual uPTR(Context) createFramebuffers(const uint32_t& width = 1600u, const uint32_t& height = 1200u) { // 
            std::cout << "Create Frame Buffer" << std::endl;

            // 
            std::array<VkImageView, 9u> deferredAttachments = {};
            std::array<VkImageView, 9u> smpFlip0Attachments = {};
            std::array<VkImageView, 9u> smpFlip1Attachments = {};

            // 
            auto allocInfo = vkt::MemoryAllocationInfo{};
            allocInfo.device = *driver;
            allocInfo.memoryProperties = driver->getMemoryProperties().memoryProperties;
            allocInfo.dispatch = driver->getDispatch();

            // 
            for (uint32_t b = 0u; b < 12u; b++) { // 
                frameBfImages[b] = vkt::ImageRegion(std::make_shared<vkt::ImageAllocation>(allocInfo, vkh::VkImageCreateInfo{
                    .format = VK_FORMAT_R32G32B32A32_SFLOAT,
                    .extent = {width,height,1u},
                    .usage = {.eTransferDst = 1, .eSampled = 1, .eStorage = 1, .eColorAttachment = 1 },
                }), vkh::VkImageViewCreateInfo{
                    .format = VK_FORMAT_R32G32B32A32_SFLOAT,
                }).setSampler(driver->device.createSampler(vkh::VkSamplerCreateInfo{
                    .magFilter = VK_FILTER_LINEAR,
                    .minFilter = VK_FILTER_LINEAR,
                    .addressModeU = VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE,
                    .addressModeV = VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE,
                    .unnormalizedCoordinates = true,
                }));
                if (b < 8u) { deferredAttachments[b] = frameBfImages[b]; };
            };

            // 
            for (uint32_t b=0u;b<12u;b++) { // 
                smFlip0Images[b] = vkt::ImageRegion(std::make_shared<vkt::ImageAllocation>(allocInfo, vkh::VkImageCreateInfo{
                    .format = VK_FORMAT_R32G32B32A32_SFLOAT,
                    .extent = {width,height,1u},
                    .usage = {.eTransferDst = 1, .eSampled = 1, .eStorage = 1, .eColorAttachment = 1 },
                }), vkh::VkImageViewCreateInfo{
                    .format = VK_FORMAT_R32G32B32A32_SFLOAT,
                }).setSampler(driver->device.createSampler(vkh::VkSamplerCreateInfo{
                    .magFilter = VK_FILTER_LINEAR,
                    .minFilter = VK_FILTER_LINEAR,
                    .addressModeU = VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE,
                    .addressModeV = VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE,
                    .unnormalizedCoordinates = true,
                }));
                if (b < 8u) { smpFlip0Attachments[b] = smFlip0Images[b]; };


                smFlip1Images[b] = vkt::ImageRegion(std::make_shared<vkt::ImageAllocation>(allocInfo, vkh::VkImageCreateInfo{
                    .format = VK_FORMAT_R32G32B32A32_SFLOAT,
                    .extent = {width,height,1u},
                    .usage = {.eTransferDst = 1, .eSampled = 1, .eStorage = 1, .eColorAttachment = 1 },
                }), vkh::VkImageViewCreateInfo{
                    .format = VK_FORMAT_R32G32B32A32_SFLOAT,
                }).setSampler(driver->device.createSampler(vkh::VkSamplerCreateInfo{
                    .magFilter = VK_FILTER_LINEAR,
                    .minFilter = VK_FILTER_LINEAR,
                    .addressModeU = VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE,
                    .addressModeV = VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE,
                    .unnormalizedCoordinates = true,
                }));
                if (b < 8u) { smpFlip1Attachments[b] = smFlip1Images[b]; };
            };

            // 
            depthImage = vkt::ImageRegion(std::make_shared<vkt::ImageAllocation>(allocInfo, vkh::VkImageCreateInfo{
                .format = VK_FORMAT_D32_SFLOAT_S8_UINT,
                .extent = {width,height,1u},
                .usage = {.eTransferDst = 1, .eSampled = 1, .eDepthStencilAttachment = 1 },
            }), vkh::VkImageViewCreateInfo{
                .format = VK_FORMAT_D32_SFLOAT_S8_UINT,
                .subresourceRange = {.aspectMask = {.eDepth = 1, .eStencil = 1}},
            }).setSampler(driver->device.createSampler(vkh::VkSamplerCreateInfo{
                .magFilter = VK_FILTER_LINEAR,
                .minFilter = VK_FILTER_LINEAR,
                .addressModeU = VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE,
                .addressModeV = VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE,
                .unnormalizedCoordinates = true,
            }));

            // 5th attachment
            deferredAttachments[8u] = depthImage;
            smpFlip0Attachments[8u] = depthImage;
            smpFlip1Attachments[8u] = depthImage;

            // 
            deferredFramebuffer = driver->getDevice().createFramebuffer(vkh::VkFramebufferCreateInfo{
                .renderPass = renderPass,
                .attachmentCount = static_cast<uint32_t>(deferredAttachments.size()),
                .pAttachments = deferredAttachments.data(),
                .width = width,
                .height = height
            });

            // Reprojection WILL NOT write own depth... 
            smpFlip0Framebuffer = driver->getDevice().createFramebuffer(vkh::VkFramebufferCreateInfo{
                .renderPass = renderPass,
                .attachmentCount = static_cast<uint32_t>(smpFlip0Attachments.size()),
                .pAttachments = smpFlip0Attachments.data(),
                .width = width,
                .height = height
            });

            // Reprojection WILL NOT write own depth... 
            smpFlip1Framebuffer = driver->getDevice().createFramebuffer(vkh::VkFramebufferCreateInfo{
                .renderPass = renderPass,
                .attachmentCount = static_cast<uint32_t>(smpFlip1Attachments.size()),
                .pAttachments = smpFlip1Attachments.data(),
                .width = width,
                .height = height
            });

            // 
            scissor = vk::Rect2D{ vk::Offset2D(0, 0), vk::Extent2D(width, height) };
            viewport = vk::Viewport{ 0.0f, 0.0f, static_cast<float>(scissor.extent.width), static_cast<float>(scissor.extent.height), 0.f, 1.f };

            // 
            thread->submitOnce([&,this](vk::CommandBuffer& cmd) {
                //vkt::imageBarrier(cmd, vkt::ImageBarrierInfo{ .image = depthImage->getImage(), .targetLayout = vk::ImageLayout::eGeneral, .originLayout = vk::ImageLayout::eUndefined, .subresourceRange = vkh::VkImageSubresourceRange(depthImage) });
                depthImage.transfer(cmd);
                for (uint32_t i = 0u; i < 12u; i++) { // Definitely Not an Hotel
                    this->smFlip1Images[i].transfer(cmd); cmd.clearColorImage(this->smFlip1Images[i], vk::ImageLayout::eGeneral, vk::ClearColorValue().setFloat32({ 0.f,0.f,0.f,0.f }), { this->smFlip1Images[i] });
                    this->smFlip0Images[i].transfer(cmd); cmd.clearColorImage(this->smFlip0Images[i], vk::ImageLayout::eGeneral, vk::ClearColorValue().setFloat32({ 0.f,0.f,0.f,0.f }), { this->smFlip0Images[i] });
                    this->frameBfImages[i].transfer(cmd); cmd.clearColorImage(this->frameBfImages[i], vk::ImageLayout::eGeneral, vk::ClearColorValue().setFloat32({ 0.f,0.f,0.f,0.f }), { this->frameBfImages[i] });
                };
            });

            // 
            return uTHIS;
        };

        // 
        virtual uPTR(Context) createDescriptorSetLayouts() { // reset layout descriptions
            std::cout << "Create Descriptor Set Layouts" << std::endl;

            this->meshDataDescriptorSetLayoutHelper = {};
            this->bindingsDescriptorSetLayoutHelper = {};
            this->samplingDescriptorSetLayoutHelper = {};
            this->deferredDescriptorSetLayoutHelper = {};
            this->materialDescriptorSetLayoutHelper = {};

            // Raw Data
            for (uint32_t b=0u;b<2u;b++) { // For Ray Tracers
                this->meshDataDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = b, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_TEXEL_BUFFER, .descriptorCount =   64u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1 } },vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1});
            };

            // IndeX Data
            this->meshDataDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 8u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_TEXEL_BUFFER, .descriptorCount = 64u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1 });

            // BETA: Ray Query Requirements
            this->bindingsDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 0u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER           , .descriptorCount =   64u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });
            this->bindingsDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 1u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER           , .descriptorCount =   64u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });
            this->bindingsDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 2u, .descriptorType = VK_DESCRIPTOR_TYPE_ACCELERATION_STRUCTURE_KHR, .descriptorCount =   1u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });
            this->bindingsDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 3u, .descriptorType = VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER           , .descriptorCount =    1u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });
            this->bindingsDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 4u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER           , .descriptorCount =   64u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });
            this->bindingsDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 5u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER           , .descriptorCount =    1u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });
            this->bindingsDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 6u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER           , .descriptorCount =    1u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 }); // Super-Instance
            this->bindingsDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 7u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER           , .descriptorCount =   64u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });
            this->bindingsDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 8u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER           , .descriptorCount =   64u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });

            // 
            this->samplingDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 0u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_IMAGE         , .descriptorCount = 12u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } },vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });
            this->samplingDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 1u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER        , .descriptorCount = 12u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } },vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });

            // 
            this->deferredDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 0u, .descriptorType = VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER, .descriptorCount = 12u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });
            this->deferredDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 1u, .descriptorType = VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER, .descriptorCount = 12u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });
            this->deferredDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 2u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_IMAGE         , .descriptorCount = 12u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });

            // 
            this->materialDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 0u, .descriptorType = VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER, .descriptorCount = 128u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });
            this->materialDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 1u, .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER        , .descriptorCount =   8u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });
            this->materialDescriptorSetLayoutHelper.pushBinding(vkh::VkDescriptorSetLayoutBinding{ .binding = 2u, .descriptorType = VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER, .descriptorCount =   1u, .stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 } }, vkh::VkDescriptorBindingFlags{ .ePartiallyBound = 1, .eVariableDescriptorCount = 1 });

            // 
            this->materialDescriptorSetLayout = driver->getDevice().createDescriptorSetLayout(materialDescriptorSetLayoutHelper);
            this->deferredDescriptorSetLayout = driver->getDevice().createDescriptorSetLayout(deferredDescriptorSetLayoutHelper);
            this->samplingDescriptorSetLayout = driver->getDevice().createDescriptorSetLayout(samplingDescriptorSetLayoutHelper);
            this->bindingsDescriptorSetLayout = driver->getDevice().createDescriptorSetLayout(bindingsDescriptorSetLayoutHelper);
            this->meshDataDescriptorSetLayout = driver->getDevice().createDescriptorSetLayout(meshDataDescriptorSetLayoutHelper);

            // 
            std::vector<vkh::VkPushConstantRange> ranges = { {.stageFlags = { .eVertex = 1, .eGeometry = 1, .eFragment = 1, .eCompute = 1, .eRaygen = 1, .eClosestHit = 1, .eMiss = 1 }, .offset = 0u, .size = 16u } };
            std::vector<VkDescriptorSetLayout> layouts = { meshDataDescriptorSetLayout, bindingsDescriptorSetLayout, deferredDescriptorSetLayout, samplingDescriptorSetLayout, materialDescriptorSetLayout };
            this->unifiedPipelineLayout = driver->getDevice().createPipelineLayout(vkh::VkPipelineLayoutCreateInfo{}.setSetLayouts(layouts).setPushConstantRanges(ranges));
            return uTHIS;
        };

        // 
        virtual uPTR(Context) createDescriptorSets() {
            std::cout << "Create Descriptor Sets" << std::endl;

            if (!this->unifiedPipelineLayout) { this->createDescriptorSetLayouts(); };

            {
                vkh::VsDescriptorSetCreateInfoHelper descInfo(deferredDescriptorSetLayout, thread->getDescriptorPool());

                { // 
                    vkh::VsDescriptorHandle<VkDescriptorImageInfo> handle = descInfo.pushDescription(vkh::VkDescriptorUpdateTemplateEntry{
                        .dstBinding = 0u,
                        .descriptorCount = 12u,
                        .descriptorType = VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER
                    });

                    // 
                    //memcpy(&handle, &frameBfImages[0u].getDescriptor(), sizeof(vk::DescriptorImageInfo) * 8u);
                    for (uint32_t i = 0; i < 12u; i++) {
                        handle.offset<VkDescriptorImageInfo>(i) = frameBfImages[i].getDescriptor();
                    };
                }

                { //
                    vkh::VsDescriptorHandle<VkDescriptorImageInfo> handle = descInfo.pushDescription(vkh::VkDescriptorUpdateTemplateEntry{
                        .dstBinding = 1u,
                        .descriptorCount = 12u,
                        .descriptorType = VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER
                    });

                    // 
                    for (uint32_t i = 0; i < 12u; i++) {
                        handle.offset<VkDescriptorImageInfo>(i) = vkt::ImageRegion(frameBfImages[i]).setSampler(driver->device.createSampler(vkh::VkSamplerCreateInfo{
                            .magFilter = VK_FILTER_LINEAR,
                            .minFilter = VK_FILTER_LINEAR,
                            .addressModeU = VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE,
                            .addressModeV = VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE
                        })).getDescriptor();
                    }
                }

                { // 
                    vkh::VsDescriptorHandle<VkDescriptorImageInfo> handle = descInfo.pushDescription(vkh::VkDescriptorUpdateTemplateEntry{
                        .dstBinding = 2u,
                        .descriptorCount = 12u,
                        .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_IMAGE
                    });

                    // 
                    for (uint32_t i = 0; i < 12u; i++) {
                        handle.offset<VkDescriptorImageInfo>(i) = vkt::ImageRegion(frameBfImages[i]).setSampler({}).getDescriptor();
                    };
                }

                // 
                this->driver->getDevice().updateDescriptorSets(vkt::vector_cast<vk::WriteDescriptorSet,vkh::VkWriteDescriptorSet>(descInfo.setDescriptorSet(
                    this->deferredDescriptorSet = driver->getDevice().allocateDescriptorSets(descInfo)[0]
                )),{});
            };

            { // For Reprojection Pipeline
                vkh::VsDescriptorSetCreateInfoHelper descInfo(samplingDescriptorSetLayout, thread->getDescriptorPool());
                vkh::VsDescriptorHandle<VkDescriptorImageInfo> handle = descInfo.pushDescription(vkh::VkDescriptorUpdateTemplateEntry{
                    .dstBinding = 0u,
                    .descriptorCount = 12u,
                    .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_IMAGE
                });

                // 
                //memcpy(&handle, &smFlip0Images[0u].getDescriptor(), sizeof(vk::DescriptorImageInfo) * 8u);
                for (uint32_t i = 0; i < 12u; i++) {
                    handle.offset<VkDescriptorImageInfo>(i) = smFlip0Images[i].getDescriptor();
                };

                // Reprojection WILL NOT write own depth... 
                this->driver->getDevice().updateDescriptorSets(vkt::vector_cast<vk::WriteDescriptorSet,vkh::VkWriteDescriptorSet>(descInfo.setDescriptorSet(
                    this->smpFlip0DescriptorSet = driver->getDevice().allocateDescriptorSets(descInfo)[0]
                )),{});
            };

            { // For Reprojection Pipeline
                vkh::VsDescriptorSetCreateInfoHelper descInfo(samplingDescriptorSetLayout, thread->getDescriptorPool());
                vkh::VsDescriptorHandle<VkDescriptorImageInfo> handle = descInfo.pushDescription(vkh::VkDescriptorUpdateTemplateEntry{
                    .dstBinding = 0u,
                    .descriptorCount = 12u,
                    .descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_IMAGE
                });

                // 
                //memcpy(&handle, &smFlip1Images[0u].getDescriptor(), sizeof(vk::DescriptorImageInfo) * 8u);
                for (uint32_t i = 0; i < 12u; i++) {
                    handle.offset<VkDescriptorImageInfo>(i) = smFlip1Images[i].getDescriptor();
                };

                // Reprojection WILL NOT write own depth... 
                this->driver->getDevice().updateDescriptorSets(vkt::vector_cast<vk::WriteDescriptorSet, vkh::VkWriteDescriptorSet>(descInfo.setDescriptorSet(
                    this->smpFlip1DescriptorSet = driver->getDevice().allocateDescriptorSets(descInfo)[0]
                )), {});
            };

            // 
            this->descriptorSets[2] = this->deferredDescriptorSet;
            this->descriptorSets[3] = this->smpFlip0DescriptorSet;

            // 
            return uTHIS;
        };

        // 
        virtual uPTR(Context) initialize(const uint32_t& width = 1600u, const uint32_t& height = 1200u) {
            this->createRenderPass();
            this->createFramebuffers(width,height);
            this->createDescriptorSetLayouts();
            this->createDescriptorSets();
            return uTHIS;
        };

    protected: // 
        std::chrono::time_point<std::chrono::steady_clock> beginTime = std::chrono::high_resolution_clock::now();
        std::chrono::time_point<std::chrono::steady_clock> leastTime = std::chrono::high_resolution_clock::now();
        std::chrono::time_point<std::chrono::steady_clock> previTime = std::chrono::high_resolution_clock::now();

        // 
        vk::Rect2D scissor = {};
        vk::Viewport viewport = {};
        vk::RenderPass renderPass = {};
        vk::Framebuffer smpFlip0Framebuffer = {};
        vk::Framebuffer smpFlip1Framebuffer = {};
        vk::Framebuffer deferredFramebuffer = {};

        // 
        vkt::Vector<Matrices> uniformGPUData = {};
        vkt::Vector<Matrices> uniformRawData = {};
        //Matrices uniformData = {};

        // Image Buffers
        std::array<vkt::ImageRegion, 12u> smFlip0Images = {};
        std::array<vkt::ImageRegion, 12u> smFlip1Images = {}; // Path Tracing
        std::array<vkt::ImageRegion, 12u> frameBfImages = {}; // Rasterization
        std::array<vk::DescriptorSet, 5u> descriptorSets = {};
        vkt::ImageRegion depthImage = {};

        // 
        vk::DescriptorSet deferredDescriptorSet = {};
        vk::DescriptorSet smpFlip0DescriptorSet = {};
        vk::DescriptorSet smpFlip1DescriptorSet = {};
        vk::PipelineLayout unifiedPipelineLayout = {};

        // 
        vk::DescriptorSetLayout materialDescriptorSetLayout = {}; // Material Descriptions
        vk::DescriptorSetLayout deferredDescriptorSetLayout = {}; // Deferred Shading Descriptions (Diffuse Texturing)
        vk::DescriptorSetLayout meshDataDescriptorSetLayout = {}; // Packed Mesh Data (8-bindings)
        vk::DescriptorSetLayout samplingDescriptorSetLayout = {}; // Framebuffers and Samples (Diffuse, Path-Tracing and ReProjection)
        vk::DescriptorSetLayout bindingsDescriptorSetLayout = {}; // Bindings, Attributes Descriptions
        
        // 
        vkh::VsDescriptorSetLayoutCreateInfoHelper materialDescriptorSetLayoutHelper = {};
        vkh::VsDescriptorSetLayoutCreateInfoHelper deferredDescriptorSetLayoutHelper = {};
        vkh::VsDescriptorSetLayoutCreateInfoHelper meshDataDescriptorSetLayoutHelper = {};
        vkh::VsDescriptorSetLayoutCreateInfoHelper samplingDescriptorSetLayoutHelper = {};
        vkh::VsDescriptorSetLayoutCreateInfoHelper bindingsDescriptorSetLayoutHelper = {};
        
        // 
        vkt::uni_ptr<Driver> driver = {};
        vkt::uni_ptr<Thread> thread = {};

        // 
        //glm::mat4 projected = glm::mat4(1.f);
        //glm::mat4 modelview = glm::mat4(1.f);
    };

};
