#pragma once

#include <iostream>
#include "jniFooJiviX.h"

class JiviX { public:
    static inline int invokeJavaCode(int a, int b) {
        return jvx_foo(a, b);
    }

    static inline void process(void *buffer, int size) {
        std::cout <<  invokeJavaCode(5, 5)  << std::endl;
        std::cout << "Processing in C++..." << std::endl;
    }
};
