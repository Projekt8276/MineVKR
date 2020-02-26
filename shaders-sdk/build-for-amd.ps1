#!/snap/bin/pwsh-preview

$CFLAGSV="--target-env spirv1.5 -V -d -t --aml --nsf -DUSE_MORTON_32 -DUSE_F32_BVH -DAMD_PLATFORM"

$VNDR="amdgpu"
. "./shaders-list.ps1"

BuildAllShaders ""

#pause for check compile errors
Pause
