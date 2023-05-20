#version 330

in vec3 passNormal;
in vec2 passTexCoord;
in vec3 toCamera;
in mat3 toTangentSpace;
in vec3 camPos;
in vec3 crntPos;

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
	if (material.useNormalTexture == 1 || material.useDisplacementTexture == 1 || material.useSpecularTexture == 1) {
		halfDir = normalize(toTangentSpace * halfDir);
	}
	float angle = max(dot(halfDir, normal), 0);
	float factor = pow(angle, 8);
	if (material.useSpecularTexture == 1 ) {
		vec3 st = vec3(1,1,1) - texture(material.specularTex, texCoord).xyz;
		factor = factor * length(st);
	}
	return factor;
}
vec4 calculateDirectionalLightColour(DirectionalLight light, vec3 normal, vec3 toCam, vec2 texCoord) {
	vec3 dir = normalize(light.direction);
	if (material.useNormalTexture == 1 || material.useDisplacementTexture == 1 || material.useSpecularTexture == 1) {
		dir = normalize(toTangentSpace * dir);
	}
	float influence = max(dot(normal, dir), 0);
	float specularFactor = calculateSpecularFactor(toCam, dir, normal, texCoord);
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
	if ((material.useDisplacementTexture == 1 || material.useSpecularTexture == 1) && material.useNormalTexture == 0) {
			return normalize(toTangentSpace * passNormal);
	} else if (material.useNormalTexture == 0) {
		return normalize(passNormal);
	}
	vec3 newNormal = texture(material.normalTex, texCoord).rgb;
	return normalize(newNormal);
}
vec2 getTextureCoordinates(vec3 toCam) {
	vec2 UVs = passTexCoord;
	if (material.useDisplacementTexture == 1) {
		vec3 viewDirection = normalize(toCam);

			// Variables that control parallax occlusion mapping quality
			float heightScale = 0.05f;
			const float minLayers = 8.0f;
		    const float maxLayers = 64.0f;
		    float numLayers = mix(maxLayers, minLayers, abs(dot(vec3(0.0f, 0.0f, 1.0f), viewDirection)));
			float layerDepth = 1.0f / numLayers;
			float currentLayerDepth = 0.0f;

			// Remove the z division if you want less aberated results
			vec2 S = viewDirection.xy / viewDirection.z * heightScale;
		    vec2 deltaUVs = S / numLayers;

			float currentDepthMapValue = 1.0f - texture(material.displacementTex, UVs).r;

			// Loop till the point on the heightmap is "hit"
			while(currentLayerDepth < currentDepthMapValue)
		    {
		        UVs -= deltaUVs;
		        currentDepthMapValue = 1.0f - texture(material.displacementTex, UVs).r;
		        currentLayerDepth += layerDepth;
		    }

			// Apply Occlusion (interpolation with prev value)
			vec2 prevTexCoords = UVs + deltaUVs;
			float afterDepth  = currentDepthMapValue - currentLayerDepth;
			float beforeDepth = 1.0f - texture(material.displacementTex, prevTexCoords).r - currentLayerDepth + layerDepth;
			float weight = afterDepth / (afterDepth - beforeDepth);
			UVs = prevTexCoords * weight + UVs * (1.0f - weight);

			// Get rid of anything outside the normal range
			if(UVs.x > 1.0 || UVs.y > 1.0 || UVs.x < 0.0 || UVs.y < 0.0)
				discard;
	}
	return UVs;
}

void main() {
	vec3 toCam = normalize(toCamera);
	vec2 texCoord = getTextureCoordinates(toCam);
	vec3 normal = getNormal(texCoord);

	vec4 diffuseColour = calculateDiffuseColour(texCoord);

	vec4 totalLightColour = calculateDirectionalLightColour(sun, normal, toCam, texCoord) + calculateAmbientLightColour(ambient);


	colour = diffuseColour * totalLightColour;

}






