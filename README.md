# Spring Security 6 / OAUTH2

1- Boas praticas de segurança em api backend
2- Autenticação e autorização com token JWT
3- Controle de permissão dos usuários granular através de roles(admins e usuários normais)
4- Integração com mysql via docker

5- Será usado um tipo de criptografia assimétrica (usando uma chave privada e uma chave publica)
 * Criando chave privada pelo terminal(wsl windows) 
	openssl genrsa > arquivo.key
 * Gerando chave publica a partir da privada
	openssl rsa -in arquivo.key -pubout -out arquivo.pub  
