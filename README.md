# SuficienciaBackend

Versão do JDK usada: 11
<br/>

## Rodar o backend

Necessário instalar o Docker-Desktop (https://www.docker.com/products/docker-desktop/), para subir uma imagem Postgres para rodar a base de dados
<br/>
No Diretório root do projeto, rodar o comando 'docker compose up -d' no seu diretório, assim rodando o arquivo docker-compose e subindo uma imagem postgres, com o nome da base de provasuficiencia, e o usuário e a senha da base de 'root', para assim se quiser acessar essa base, no DBeaver por exemplo
<br/>
Caso queira apagar a base é so rodar 'docker compose down -v', que irá derrubar a imagem, e depois 'docker-compose up -d' novamente
<br/>
Importar o projeto como Maven, instalar as dependências e estar com a parte 8080 livre
<br/>
Ao rodar o projeto será gerado o Usuário padrão ADMIN do projeto
<br/>
Usuário: admin
<br/>
Senha: admin