#!/snap/bin/pwsh-preview

$CFLAGSV="--target-env spirv1.5 -V -d -t --aml --nsf -DUSE_MORTON_32 -DUSE_F32_BVH -DNVIDIA_PLATFORM -DENABLE_TURING_INSTRUCTION_SET turing.conf"

$VNDR="turing"
. "./shaders-list.ps1"

BuildAllShaders "" "radix-rapid/"

#pause for check compile errors
Pause
