#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec3 tangent;
layout(location = 4) in vec3 bitangent;

out vec3 passNormal;
out vec2 texCoord;
out vec3 toCamera;
out mat3 toTangentSpace;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main() {


	vec4 worldposition =   transformationMatrix * vec4(position, 1);

	gl_Position = projectionMatrix * viewMatrix *  worldposition;


	passNormal = (transformationMatrix * vec4(normal, 0)).xyz;
	vec3 ttangent = (transformationMatrix * vec4(tangent, 0)).xyz;
	vec3 tbitangent = (transformationMatrix * vec4(bitangent, 0)).xyz;
	toTangentSpace = mat3(ttangent, tbitangent, passNormal);
	texCoord = textureCoord;
	toCamera = ((inverse(viewMatrix) * vec4(0,0,0,1)).xyz - worldposition.xyz);
}
