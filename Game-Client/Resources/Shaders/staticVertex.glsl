#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoord;
layout(location = 2) in vec3 normal;

out vec3 passNormal;
out vec2 texCoord;
out vec3 toCamera;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main() {


	vec4 worldposition =   transformationMatrix * vec4(position, 1);

	gl_Position = projectionMatrix * viewMatrix *  worldposition;

	passNormal = (transformationMatrix * vec4(normal, 0)).xyz;
	texCoord = textureCoord;
	toCamera = (inverse(viewMatrix) * vec4(0,0,0,1)).xyz - worldposition.xyz;
}
