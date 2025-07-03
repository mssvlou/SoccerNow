# create 8 clubes

curl -X POST "http://localhost:8080/api/clubes" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Clube Exemplo 1"}'

curl -X POST "http://localhost:8080/api/clubes" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Clube Exemplo 2"}'

curl -X POST "http://localhost:8080/api/clubes" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Clube Exemplo 3"}'

curl -X POST "http://localhost:8080/api/clubes" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Clube Exemplo 4"}'

curl -X POST "http://localhost:8080/api/clubes" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Clube Exemplo 5"}'

curl -X POST "http://localhost:8080/api/clubes" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Clube Exemplo 6"}'

curl -X POST "http://localhost:8080/api/clubes" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Clube Exemplo 7"}'

curl -X POST "http://localhost:8080/api/clubes" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Clube Exemplo 8"}'

# create 8 jogadores

curl -X POST "http://localhost:8080/api/jogadores" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Jogador Exemplo 1", "email": "JogadorExemplo1@mail.com", "password": "string", "posicao": "GUARDA_REDES", "clubeID": 1}'

curl -X POST "http://localhost:8080/api/jogadores" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Jogador Exemplo 2", "email": "JogadorExemplo2@mail.com", "password": "string", "posicao": "FIXO", "clubeID": 1}'

curl -X POST "http://localhost:8080/api/jogadores" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Jogador Exemplo 3", "email": "JogadorExemplo3@mail.com", "password": "string", "posicao": "ALA", "clubeID": 1}'

curl -X POST "http://localhost:8080/api/jogadores" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Jogador Exemplo 4", "email": "JogadorExemplo4@mail.com", "password": "string", "posicao": "ALA", "clubeID": 1}'

curl -X POST "http://localhost:8080/api/jogadores" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Jogador Exemplo 5", "email": "JogadorExemplo5@mail.com", "password": "string", "posicao": "PIVO", "clubeID": 1}'

curl -X POST "http://localhost:8080/api/jogadores" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Jogador Exemplo 6", "email": "JogadorExemplo6@mail.com", "password": "string", "posicao": "GUARDA_REDES", "clubeID": 2}'

curl -X POST "http://localhost:8080/api/jogadores" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Jogador Exemplo 7", "email": "JogadorExemplo7@mail.com", "password": "string", "posicao": "FIXO", "clubeID": 2}'

curl -X POST "http://localhost:8080/api/jogadores" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Jogador Exemplo 8", "email": "JogadorExemplo8@mail.com", "password": "string", "posicao": "ALA", "clubeID": 2}'

curl -X POST "http://localhost:8080/api/jogadores" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Jogador Exemplo 9", "email": "JogadorExemplo9@mail.com", "password": "string", "posicao": "ALA", "clubeID": 2}'

curl -X POST "http://localhost:8080/api/jogadores" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Jogador Exemplo 10", "email": "JogadorExemplo10@mail.com", "password": "string", "posicao": "ALA", "clubeID": 2}'

# create 2 árbitros

curl -X POST "http://localhost:8080/api/arbitros" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Arbitro Exemplo 1", "email": "ArbitroExemplo1@mail.com", "password": "string", "certificado": true}'

curl -X POST "http://localhost:8080/api/arbitros" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Arbitro Exemplo 2", "email": "ArbitroExemplo2@mail.com", "password": "string", "certificado": false}'

# create 2 equipas

curl -X POST "http://localhost:8080/api/equipas" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Equipa Exemplo 1", "clubeID": 1, "jogadorIDs": [1,2,3,4,5]}'

curl -X POST "http://localhost:8080/api/equipas" \
     -H "Content-Type: application/json" \
     -d '{"nome": "Equipa Exemplo 2", "clubeID": 2, "jogadorIDs": [6,7,8,9,10]}'