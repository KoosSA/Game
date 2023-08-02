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
	vec3 normal = normalize(passNormal);

	float sunFactor = max(dot(normal, sun.direction), 0);


	colour = vec4(col* (sunFactor * sun.colour) + (ambient.colour * ambient.intensity) ,1)  ;

}






