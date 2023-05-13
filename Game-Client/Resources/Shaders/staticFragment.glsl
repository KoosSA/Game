#version 330

in vec3 passNormal;
in vec2 texCoord;

out vec4 colour;

struct DirectionalLight {
	vec3 direction;
	vec3 colour;
	float intensity;
};

struct Material {
	vec4 colour;
	float useTexture;
	sampler2D diffuseTex;
};

uniform DirectionalLight sun;
uniform Material material;

vec4 calculateDirColour(DirectionalLight light) {
	normalize(light.direction);
	vec3 toLight = -light.direction;
	float influence = max(dot(passNormal, toLight), 0.05) * light.intensity;
	return vec4(light.colour * influence, 1);
}

vec4 calculateDiffuseColour() {
	vec4 col = vec4(1,1,1,1);
	if (material.useTexture == 0) {
		col = material.colour;
	} else {
		col = texture(material.diffuseTex, texCoord);
	}
	return col;
}

void main() {
	normalize(passNormal);
	vec4 dirLightColour = calculateDirColour(sun);
	vec4 diffuseColour = calculateDiffuseColour();

	colour = diffuseColour * dirLightColour;

}






