#version 330

in vec3 passNormal;
in vec2 texCoord;

out vec4 colour;

struct DirectionalLight {
	vec3 direction;
	vec3 colour;
	float intensity;
};
struct AmbientLight {
	vec3 colour;
	float intensity;
};

struct Material {
	vec4 colour;
	float useTexture;
	sampler2D diffuseTex;
};

uniform DirectionalLight sun;
uniform AmbientLight ambient;
uniform Material material;

vec4 calculateDirectionalLightColour(DirectionalLight light) {
	normalize(light.direction);
	float influence = max(dot(passNormal, light.direction), 0);
	return vec4(light.colour.xyz * influence, 1);
}
vec4 calculateAmbientLightColour(AmbientLight light) {
	return vec4(light.colour.xyz * light.intensity, 1);
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


	vec4 diffuseColour = calculateDiffuseColour();

	vec4 totalLightColour = calculateDirectionalLightColour(sun) + calculateAmbientLightColour(ambient);

	//normalize(totalLightColour);

	colour = diffuseColour * totalLightColour;

}






