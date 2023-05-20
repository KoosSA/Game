#version 330

in vec3 passNormal;
in vec2 texCoord;
in vec3 toCamera;
in mat3 toTangentSpace;

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
	int useDiffuseTexture;
	int useNormalTexture;
	sampler2D diffuseTex;
	sampler2D normalTex;
};

uniform DirectionalLight sun;
uniform AmbientLight ambient;
uniform Material material;

float calculateSpecularFactor(vec3 toCam, vec3 lightDir, vec3 normal) {
	vec3 halfDir = normalize(lightDir + toCam);
	if (material.useNormalTexture == 1) {
		halfDir = normalize(toTangentSpace * halfDir);
	}
	float angle = max(dot(halfDir, normal), 0);
	return pow(angle, 8);
}
vec4 calculateDirectionalLightColour(DirectionalLight light, vec3 normal, vec3 toCam) {
	vec3 dir = normalize(light.direction);
	if (material.useNormalTexture == 1) {
		dir = normalize(toTangentSpace * dir);
	}
	float influence = max(dot(normal, dir), 0);
	float specularFactor = calculateSpecularFactor(toCam, dir, normal);
	return vec4(light.colour.xyz * ((influence + specularFactor)), 1);
}
vec4 calculateAmbientLightColour(AmbientLight light) {
	return vec4(light.colour.xyz * light.intensity, 1);
}

vec4 calculateDiffuseColour() {
	vec4 col = vec4(1,1,1,1);
	if (material.useDiffuseTexture == 0) {
		return material.colour;
	} else {
		col = texture(material.diffuseTex, texCoord);
	}
	return col;
}
vec3 getNormal() {
	if (material.useNormalTexture == 0) {
		return normalize(passNormal);
	}
	vec3 newNormal = texture(material.normalTex, texCoord).rgb;
	return normalize(newNormal);
}

void main() {
	vec3 normal = getNormal();
	vec3 toCam = normalize(toCamera);


	vec4 diffuseColour = calculateDiffuseColour();

	vec4 totalLightColour = calculateDirectionalLightColour(sun, normal, toCam);// + calculateAmbientLightColour(ambient);


	colour = diffuseColour * totalLightColour;

}






