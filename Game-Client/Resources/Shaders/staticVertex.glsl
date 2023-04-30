#version 330

layout(location = 0) in vec3 position;

//uniform mat4 transformationMatrix;

void main() {

	//vec4 worldposition = transformationMatrix * vec4(position, 1);

	gl_Position = vec4(position, 1);
}
