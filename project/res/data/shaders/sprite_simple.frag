#version 150

//In
in vec2 texCoord;
in float fogAmount;

//Uniforms
uniform float editorFilter = 0.0;
uniform sampler2D baseColor;
uniform vec4 baseColorMod = vec4(1.0, 1.0, 1.0, 1.0);
uniform vec3 fogColor = vec3(0.929, 0.906, 0.753);

//Out
out vec4 FragmentColor;

void main() {
    vec4 textureSample = texture(baseColor, texCoord);
    
    if(textureSample.a <= 0) discard;

    FragmentColor = textureSample*baseColorMod;
    
    //Fog
    FragmentColor = mix(FragmentColor, vec4(fogColor, FragmentColor.a), fogAmount);
    
    //Editor filter
    FragmentColor = mix(FragmentColor, vec4(vec3(1.0, 0.964, .839), FragmentColor.a), editorFilter );
}
