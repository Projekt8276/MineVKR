// #
#define VMA_IMPLEMENTATION
#define TINYOBJLOADER_IMPLEMENTATION
#define VKT_FORCE_VMA_IMPLEMENTATION
#define VK_ENABLE_BETA_EXTENSIONS
//#define VKT_ENABLE_GLFW_SUPPORT

#ifdef WIN32
#define VK_USE_PLATFORM_WIN32_KHR
#endif

#include <glad/glad.h>
#include <GLFW/glfw3.h>
#include <vkt2/fw.hpp>
#include <JiviX/JiviX.hpp>

#define TINYGLTF_IMPLEMENTATION
#define STB_IMAGE_IMPLEMENTATION
#define STB_IMAGE_WRITE_IMPLEMENTATION
#include "misc/tiny_gltf.h"

#include <iostream>


class RayTracer : public std::enable_shared_from_this<RayTracer> { public: 
	 ~RayTracer() {};
	  RayTracer() {
		  this->fw = std::make_shared<vkt::GPUFramework>();
	};

	
	std::shared_ptr<vkt::GPUFramework> fw = {};
};

GLuint handleError() {
    GLuint errorCode = 0;
    //std::cout << (errorCode = glGetError()) << std::endl;
    assert(errorCode == 0);
    return errorCode;
}


void framebuffer_size_callback(GLFWwindow* window, int width, int height);
void processInput(GLFWwindow *window);

// settings
const unsigned int SCR_WIDTH = 1600;
const unsigned int SCR_HEIGHT = 1200;

const char *vertexShaderSource = "#version 460 compatibility\n"
    "layout (location = 0) in vec4 aPos;\n" 
    "void main()\n"
    "{\n"
    "   gl_Position = vec4(aPos.xyz, 1.0f);\n"
    "}\0";

const char *fragmentShaderSource = "#version 460 compatibility\n"
    "#extension GL_ARB_bindless_texture : require\n"
    "out vec4 FragColor;\n"
    "layout (binding = 0) uniform sampler2D texture0;\n"
    "void main()\n"
    "{\n"
	"	vec2 tx = gl_FragCoord.xy/vec2(1600.f,1200.f);\n"
    "   FragColor = vec4(pow(texture(texture0,tx).xyz,1.f.xxx/2.2.xxx),1.f);\n"
    "   //FragColor = vec4(pow(texture(texture1,tx).xyz/texture(texture1,tx).w*vec3(0.5f,0.5f,1.f),1.f.xxx/2.2.xxx),1.f);\n"
    "   //FragColor = vec4(pow(texture(texture0,tx).xyz,1.f.xxx/2.2.xxx),1.f);\n"
    "}\n\0";


const char* vertexTFShaderSource = "#version 460 compatibility\n"
    "layout (location = 0) in vec3 aPos;\n"
    "layout (location = 0) out vec3 oPos;\n"

    "void main()\n"
    "{\n"
    "   gl_Position = vec4(oPos = aPos, 1.f);\n"
    "}\0";


const char* geometryTFShaderSource = "#version 460 compatibility\n"
    "layout (location = 0) in vec3 oPos[];\n"

    "layout (location = 0, xfb_buffer = 0, xfb_stride = 80, xfb_offset = 0) out vec4 fPosition;\n"
    "layout (location = 1, xfb_buffer = 0, xfb_stride = 80, xfb_offset = 16) out vec4 fTexcoord;\n"
    "layout (location = 2, xfb_buffer = 0, xfb_stride = 80, xfb_offset = 32) out vec4 fNormal;\n"
    "layout (location = 3, xfb_buffer = 0, xfb_stride = 80, xfb_offset = 48) out vec4 fTangent;\n"
    "layout (location = 4, xfb_buffer = 0, xfb_stride = 80, xfb_offset = 64) out vec4 fBinormal;\n"

    "layout (lines_adjacency) in; \n"
    "layout (triangle_strip, max_vertices = 4) out; \n"

    "void main()\n"
    "{\n"
    "for (int i=0;i<4;i++) {\n"
    "   gl_Position = vec4(oPos[i].xyz, 1.f);\n"
    "   fNormal = vec4(vec3(0.f,0.f,1.f), 1.f);\n"
    "   fPosition = vec4(oPos[i].xyz, 1.0f);\n"
    "   EmitVertex();\n"
    "}\n"
    "EndPrimitive();\n"
    "}\0";



struct FDStruct {
    glm::vec4 fPosition = glm::vec4(1.f);
    glm::vec4 fTexcoord = glm::vec4(1.f);
    glm::vec4 fNormal = glm::vec4(1.f);
    glm::vec4 fTangent = glm::vec4(1.f);
    glm::vec4 fBinormal = glm::vec4(1.f);
};


int main()
{
    // glfw: initialize and configure
    // ------------------------------
    glfwInit();
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);

#ifdef __APPLE__
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); // uncomment this statement to fix compilation on OS X
#endif

    // glfw window creation
    // --------------------
    GLFWwindow* window = glfwCreateWindow(SCR_WIDTH, SCR_HEIGHT, "LearnOpenGL", NULL, NULL);
    if (window == NULL)
    {
        std::cout << "Failed to create GLFW window" << std::endl;
        glfwTerminate();
        return -1;
    }
    glfwMakeContextCurrent(window);
    glfwSetFramebufferSizeCallback(window, framebuffer_size_callback);






	// initialize Vulkan
    auto fw = jvx::Driver();



    // build and compile our shader program
    // ------------------------------------
    const int vertexTFShader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexTFShader, 1, &vertexTFShaderSource, NULL);
    glCompileShader(vertexTFShader);
    // check for shader compile errors
    int success; char infoLog[512];
    glGetShaderiv(vertexTFShader, GL_COMPILE_STATUS, &success);
    if (!success)
    {
        glGetShaderInfoLog(vertexTFShader, 512, NULL, infoLog);
        std::cout << "ERROR::SHADER::VERTEX::COMPILATION_FAILED\n" << infoLog << std::endl;
    }

    // 
    const int geometryTFShader = glCreateShader(GL_GEOMETRY_SHADER);
    glShaderSource(geometryTFShader, 1, &geometryTFShaderSource, NULL);
    glCompileShader(geometryTFShader);
    // check for shader compile errors
    glGetShaderiv(geometryTFShader, GL_COMPILE_STATUS, &success);
    if (!success)
    {
        glGetShaderInfoLog(geometryTFShader, 512, NULL, infoLog);
        std::cout << "ERROR::SHADER::VERTEX::COMPILATION_FAILED\n" << infoLog << std::endl;
    }

    // link shaders
    const int shaderTFProgram = glCreateProgram();
    const char* varyings[] = { "fPosition", "fTexcoord", "fNormal", "fTangent", "fBinormal" };
    glTransformFeedbackVaryings(shaderTFProgram, 5, varyings, GL_INTERLEAVED_ATTRIBS);
    glAttachShader(shaderTFProgram, vertexTFShader);
    glAttachShader(shaderTFProgram, geometryTFShader);
    glLinkProgram(shaderTFProgram);
    // check for linking errors
    glGetProgramiv(shaderTFProgram, GL_LINK_STATUS, &success);
    if (!success) {
        glGetProgramInfoLog(shaderTFProgram, 512, NULL, infoLog);
        std::cout << "ERROR::SHADER::PROGRAM::LINKING_FAILED\n" << infoLog << std::endl;
    }
    glDeleteShader(vertexTFShader);



    // vertex shader
    const int vertexShader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexShader, 1, &vertexShaderSource, NULL);
    glCompileShader(vertexShader);
    // check for shader compile errors
    glGetShaderiv(vertexShader, GL_COMPILE_STATUS, &success);
    if (!success)
    {
        glGetShaderInfoLog(vertexShader, 512, NULL, infoLog);
        std::cout << "ERROR::SHADER::VERTEX::COMPILATION_FAILED\n" << infoLog << std::endl;
    }

    // fragment shader
    const int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragmentShader, 1, &fragmentShaderSource, NULL);
    glCompileShader(fragmentShader);
    // check for shader compile errors
    glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, &success);
    if (!success)
    {
        glGetShaderInfoLog(fragmentShader, 512, NULL, infoLog);
        std::cout << "ERROR::SHADER::FRAGMENT::COMPILATION_FAILED\n" << infoLog << std::endl;
    }

    // link shaders
    const int shaderProgram = glCreateProgram();
    glAttachShader(shaderProgram, vertexShader);
    glAttachShader(shaderProgram, fragmentShader);
    glLinkProgram(shaderProgram);
    // check for linking errors
    glGetProgramiv(shaderProgram, GL_LINK_STATUS, &success);
    if (!success) {
        glGetProgramInfoLog(shaderProgram, 512, NULL, infoLog);
        std::cout << "ERROR::SHADER::PROGRAM::LINKING_FAILED\n" << infoLog << std::endl;
    }
    glDeleteShader(vertexShader);
    glDeleteShader(fragmentShader);


    // 
    float tf_vertices[] = {
        -1.f, -1.f, -1.f,
         1.f, -1.f, -1.f,
         0.f,  1.f, -1.f,
         2.f,  1.f, -1.f
    };

    unsigned int TFVBO, TFVAO;
    glGenVertexArrays(1, &TFVAO);
    glBindVertexArray(TFVAO);

    glGenBuffers(1, &TFVBO);
    glBindBuffer(GL_ARRAY_BUFFER, TFVBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(tf_vertices), tf_vertices, GL_STATIC_DRAW);

    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);



    // 
    float vertices[] = {
        -1.f, -1.f, 0.f, // left  
         1.f, -1.f, 0.f, // right 
         1.f,  1.f, 0.f, // top   
        -1.f,  1.f, 0.f
    };

    unsigned int VBO, VAO;
    glGenVertexArrays(1, &VAO);
    glBindVertexArray(VAO);

    glGenBuffers(1, &VBO);
    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);

    //std::vector<FDStruct> outScript(3);



	auto instance = fw->createInstance();
	auto physicalDevice = fw->getPhysicalDevice(0u);
	auto device = fw->createDevice(true,"./",false);
	auto queue = fw->getQueue();
	auto commandPool = fw->getCommandPool();

    // 
    auto context = jvx::Context(fw);
    auto node = jvx::Node(context);
    auto material = jvx::Material(context);
    auto renderer = jvx::Renderer(context);
    auto meshTest0 = jvx::MeshInput(context);
    auto meshTest1 = jvx::MeshInput(context);
    auto meshBinding = jvx::MeshBinding(context, 1024u, std::vector<vk::DeviceSize>{ 1024u, 1024u });
    auto bufferViewS = jvx::BufferViewSet(context);


	// initialize renderer
    context->initialize(SCR_WIDTH, SCR_HEIGHT);
    renderer->linkMaterial(material)->linkNode(node);

    // 
    auto TRS = vkt::Vector<glm::mat3x4>(std::make_shared<vkt::VmaBufferAllocation>(fw->getAllocator(), vkh::VkBufferCreateInfo{
        .size = sizeof(glm::mat3x4) * 2ull, .usage = {.eTransferSrc = 1, .eStorageTexelBuffer = 1, .eStorageBuffer = 1, .eIndexBuffer = 1, .eVertexBuffer = 1, .eSharedDeviceAddress = 1 },
    }, VMA_MEMORY_USAGE_CPU_TO_GPU));

    // 
    TRS[0] = glm::mat3x4(1.f);
    TRS[1] = glm::transpose(glm::translate(glm::mat4x4(1.f), glm::vec3(0.5f, -0.5f, 0.f)));

    // 
    meshBinding->setTransformData(TRS)->addRangeInput(2u)->setIndexCount(6u);

    // 
    node->pushInstance(vkh::VsGeometryInstance{
        .instanceId = uint32_t(node->pushMesh(meshBinding->sharedPtr())),
        .mask = 0xFF,
        .instanceOffset = 0,
        .flags = {}
    });

	// Orange
    jvi::MaterialUnit mdk = {};
    mdk.diffuse = glm::vec4(1.f,0.75f,0.f,1.f);
    mdk.diffuseTexture = -1;
	material->pushMaterial(mdk);

    // Blue
    mdk.diffuse = glm::vec4(0.f, 0.75f, 1.f, 1.f);
    mdk.diffuseTexture = -1;
    material->pushMaterial(mdk);

    // 
    renderer->setupCommands();

	// 
	struct ShareHandles {
		//HANDLE memory{ INVALID_HANDLE_VALUE };
		HANDLE glReady{ INVALID_HANDLE_VALUE };
		HANDLE glComplete{ INVALID_HANDLE_VALUE };
	} handles;

	// 
	struct Semaphores {
        vk::Semaphore glReady = {}, glComplete = {};
	} semaphores;

	{ // 
		auto handleType = vk::ExternalSemaphoreHandleTypeFlagBits::eOpaqueWin32;
		{
			vk::SemaphoreCreateInfo sci;
			vk::ExportSemaphoreCreateInfo esci;
			sci.pNext = &esci;
			esci.handleTypes = handleType;
			semaphores.glReady = device.createSemaphore(sci);
			semaphores.glComplete = device.createSemaphore(sci);
		}
		handles.glReady = device.getSemaphoreWin32HandleKHR({ semaphores.glReady, handleType }, fw->getDispatch());
		handles.glComplete = device.getSemaphoreWin32HandleKHR({ semaphores.glComplete, handleType }, fw->getDispatch());
	}

	// Platform specific import.  On non-Win32 systems use glImportSemaphoreFdEXT instead
    GLuint glReady = 0;
    glGenSemaphoresEXT(1, &glReady);
	glImportSemaphoreWin32HandleEXT(glReady, GL_HANDLE_TYPE_OPAQUE_WIN32_EXT, handles.glReady);
    handleError();

    // 
    GLuint glComplete = 0;
    glGenSemaphoresEXT(1, &glComplete);
	glImportSemaphoreWin32HandleEXT(glComplete, GL_HANDLE_TYPE_OPAQUE_WIN32_EXT, handles.glComplete);
    handleError();

    // 
    GLuint color = context->getFrameBuffers()[8].getGL();
    glTextureParameteri(color, GL_TEXTURE_WRAP_S, GL_REPEAT);	// set texture wrapping to GL_REPEAT (default wrapping method)
    glTextureParameteri(color, GL_TEXTURE_WRAP_T, GL_REPEAT);
    glTextureParameteri(color, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTextureParameteri(color, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    // 
    GLenum layoutSignal = GL_LAYOUT_GENERAL_EXT;
    GLenum layoutWait = GL_LAYOUT_GENERAL_EXT;

    // 
    GLuint64 colorHandle = 0;

    // 
    auto GLID = meshBinding->getBindingBufferGL();

    // 
    while (!glfwWindowShouldClose(window))
    {
        processInput(window);

        // 
        //glBindTransformFeedback(GL_TRANSFORM_FEEDBACK, transformFeedback);

        glBindBufferBase(GL_TRANSFORM_FEEDBACK_BUFFER, 0u, GLID);

        // 
        glUseProgram(shaderTFProgram);
        glBindVertexArray(TFVAO);

        // 
        glEnable(GL_RASTERIZER_DISCARD);
        glBeginTransformFeedback(GL_TRIANGLES);
        glDrawArrays(GL_LINES_ADJACENCY, 0, 4);
        glEndTransformFeedback();

        // 
        glSignalSemaphoreEXT(glComplete, 0, nullptr, 1, &color, &layoutSignal);

		// 
		std::vector<vk::PipelineStageFlags> waitStages = { vk::PipelineStageFlagBits::eFragmentShader | vk::PipelineStageFlagBits::eComputeShader | vk::PipelineStageFlagBits::eRayTracingShaderKHR };
		context->getThread()->submitCmd(std::vector<vk::CommandBuffer>{ renderer->refCommandBuffer() }, vk::SubmitInfo()
            .setPWaitDstStageMask(waitStages.data())
			.setPWaitSemaphores  (&semaphores.glComplete)  .setWaitSemaphoreCount(1)
            .setPSignalSemaphores(&semaphores.glReady   ).setSignalSemaphoreCount(1));

        // 
        glWaitSemaphoreEXT(glReady, 0, nullptr, 1, &color, &layoutWait);

        // 
        //glGetNamedBufferSubData(GLID, 0u, outScript.size() * sizeof(FDStruct), outScript.data());

		// 
        glDisable(GL_RASTERIZER_DISCARD);
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glBindTextureUnit(0, color);
        glUseProgram(shaderProgram);
        glBindVertexArray(VAO);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);

		// 
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

	// 
    glDeleteVertexArrays(1, &VAO);
    glDeleteBuffers(1, &VBO);

    glfwTerminate();
    return 0;
}

void processInput(GLFWwindow *window)
{
    if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
        glfwSetWindowShouldClose(window, true);
}

void framebuffer_size_callback(GLFWwindow* window, int width, int height)
{
    // make sure the viewport matches the new window dimensions; note that width and 
    // height will be significantly larger than specified on retina displays.
    glViewport(0, 0, width, height);
}