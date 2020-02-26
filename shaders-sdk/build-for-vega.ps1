#!/snap/bin/pwsh-preview

 $CFLAGSV="--target-env spirv1.5 -V -d -t --aml --nsf -DUSE_MORTON_32 -DUSE_F32_BVH -DAMD_PLATFORM -DENABLE_VEGA_INSTRUCTION_SET"
#$CFLAGSV="--target-env spirv1.5 -V -d -t --aml --nsf -DUSE_MORTON_32 -DUSE_F16_BVH -DAMD_PLATFORM -DENABLE_VEGA_INSTRUCTION_SET"

$VNDR="vega10"
. "./shaders-list.ps1"

BuildAllShaders "" "radix-rapid/"

#pause for check compile errors
Pause
