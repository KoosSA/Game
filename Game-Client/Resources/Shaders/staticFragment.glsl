#version 330

in vec3 passNormal;
in vec2 texCoord;
in vec3 toCamera;

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

float calculateSpecularFactor(vec3 toCam, vec3 lightDir, vec3 normal) {
	//vec3 reflectedDir = reflect(-lightDir, normal);
	//return pow(max(dot(toCam, reflectedDir), 0), 50);
	vec3 halfDir = normalize(lightDir + toCam);
	float angle = max(dot(halfDir, normal), 0);
	return pow(angle, 30);
}
vec4 calculateDirectionalLightColour(DirectionalLight light, vec3 normal, vec3 toCam) {
	normalize(light.direction);
	float influence = max(dot(normal, light.direction), 0);
	float specularFactor = calculateSpecularFactor(toCam, light.direction, normal);
	return vec4(light.colour.xyz * (influence + specularFactor), 1);
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
	vec3 normal = normalize(passNormal);
	vec3 toCam = normalize(toCamera);


	vec4 diffuseColour = calculateDiffuseColour();

	vec4 totalLightColour = calculateDirectionalLightColour(sun, normal, toCam) /*+ calculateAmbientLightColour(ambient)*/;

	//normalize(totalLightColour);

	colour = diffuseColour * totalLightColour;

}






