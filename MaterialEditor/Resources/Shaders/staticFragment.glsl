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
	int useBaseColourTexture;
	int useNormalTexture;
	int useRoughnessTexture;
	int useDisplacementTexture;
	int useMetallicTexture;
	sampler2D baseColourTex;			//Texture unit 0
	sampler2D normalTex;			//Texture unit 1
	sampler2D roughnessTex;			//Texture unit 2
	sampler2D displacementTex;		//Texture unit 3
	sampler2D metallicTex;			//Texture unit 4
	float metalness;
	float shineDampener;
	float roughness;
};

uniform DirectionalLight sun;
uniform AmbientLight ambient;
uniform Material material;

vec4 calculateSpecularColor(vec3 toCam, vec3 toLight, vec3 normal, vec2 texCoord, vec3 lightColor, vec3 baseColor) {
	vec3 lightDir = normalize(-toLight);
	vec3 reflectedDir = reflect(lightDir, normal);
	float result = max(dot(reflectedDir, toCam), 0.0);
	//vec3 halfvec = normalize(lightDir + toCam);
	//float result = max(dot(halfvec, normal), 0.0);
	float dampedResult = pow(result, material.shineDampener);
	float roug = 1 - clamp(material.roughness, 0, 1);
	if (material.useRoughnessTexture == 1) {
		roug = 1 - texture(material.roughnessTex, texCoord).a;
	}
	float metallness = max(min(material.metalness, 0.0), 1.0);
	if (material.useMetallicTexture == 1) {
		metallness = 1.0 - texture(material.metallicTex, texCoord).a;
	}
	vec3 specC = baseColor;
	if (metallness == 1) {
		specC = lightColor;
	}
	return vec4(dampedResult * roug * specC, 1.0);
}

vec4 calculateDirectionalLightColour(DirectionalLight light, vec3 normal, vec3 toCam, vec2 texCoord) {
	vec3 toLight = normalize(light.direction);
	float influence = max(dot(normal, toLight), 0);
	return vec4((light.colour.xyz * influence) , 1.0);
}

vec4 calculateAmbientLightColour(AmbientLight light) {
	return vec4(light.colour.xyz * light.intensity, 1);
}

vec4 calculateDiffuseColour(vec2 texCoord) {
	vec4 col = material.colour;
	if (material.useBaseColourTexture == 1) {
		col = texture(material.baseColourTex, texCoord);
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
	vec4 dirLightColour = calculateDirectionalLightColour(sun, normal, toCam, texCoord);
	vec4 ambientLightColour = calculateAmbientLightColour(ambient);
	vec4 specularDirLightColour = calculateSpecularColor(toCam, sun.direction, normal, texCoord, sun.colour, diffuseColour.xyz);

	colour = (diffuseColour ) * (dirLightColour + ambientLightColour + specularDirLightColour) ;

}






