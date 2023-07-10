#version 330

in vec3 passNormal;
in vec2 passTexCoord;
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

uniform DirectionalLight sun;
uniform AmbientLight ambient;
uniform vec3 col;


void main() {

	colour = vec4(col ,1) ;

}






