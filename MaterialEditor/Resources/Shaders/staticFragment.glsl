#version 330

in vec3 passNormal;
in vec2 passTexCoord;
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
	int useSpecularTexture;
	int useDisplacementTexture;
	sampler2D diffuseTex;
	sampler2D normalTex;
	sampler2D specularTex;
	sampler2D displacementTex;
};

uniform DirectionalLight sun;
uniform AmbientLight ambient;
uniform Material material;

float calculateSpecularFactor(vec3 toCam, vec3 lightDir, vec3 normal, vec2 texCoord) {
	vec3 halfDir = normalize(lightDir + toCam);
	float angle = max(dot(halfDir, normal), 0);
	float factor = pow(angle, 8);
	if (material.useSpecularTexture == 1 ) {
		//vec3 st = vec3(1,1,1) - texture(material.specularTex, texCoord).xyz;
		float tex = 1 - texture(material.specularTex, texCoord).r;
		factor = factor * tex; //* length(st);
	}
	return factor;
}
vec4 calculateDirectionalLightColour(DirectionalLight light, vec3 normal, vec3 toCam, vec2 texCoord) {
	vec3 toLight = normalize(light.direction);
	float influence = max(dot(normal, toLight), 0);
	float specularFactor = calculateSpecularFactor(toCam, toLight, normal, texCoord);
	return vec4(light.colour.xyz * ((influence + specularFactor)), 1);
}

vec4 calculateAmbientLightColour(AmbientLight light) {
	return vec4(light.colour.xyz * light.intensity, 1);
}

vec4 calculateDiffuseColour(vec2 texCoord) {
	vec4 col = vec4(1,1,1,1);
	if (material.useDiffuseTexture == 0) {
		return material.colour;
	} else {
		col = texture(material.diffuseTex, texCoord);
	}
	return col;
}

vec3 getNormal(vec2 texCoord) {
	vec3 newNormal = passNormal;
	if (material.useNormalTexture == 1) {
		newNormal = texture(material.normalTex, texCoord).rgb * 2.0f - 1.0f;
		newNormal = toTangentSpace * newNormal;
	}
	return normalize(newNormal);
}

vec2 getTextureCoordinates() {
	vec2 UVs = passTexCoord;
	return UVs;
}

void main() {
	vec3 toCam = normalize(toCamera);
	vec2 texCoord = getTextureCoordinates();
	vec3 normal = getNormal(texCoord);

	vec4 diffuseColour = calculateDiffuseColour(texCoord);

	vec4 totalLightColour = calculateDirectionalLightColour(sun, normal, toCam, texCoord) + calculateAmbientLightColour(ambient);


	colour = diffuseColour * totalLightColour;

}






