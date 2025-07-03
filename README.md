# SoccerNow
**Projeto de grupo desenvolvido no âmbito da cadeira de Construção de Sistemas de Software**
## Objetivo
Desenvolver um sistema de gestão de jogos e torneios de futsal, maioritariamente focado na criação, atualização, remoção e busca de entidades
<details>
  
  <summary><b>Contextualização fornecida no enunciado</b></summary>

  > O clube desportivo Ciências Soccer Society (CSS) organiza, de forma recorrente, um conjunto de jogos e torneios de futsal. Surge, assim, a necessidade de um sistema que cubra todas as etapas deste processo.

  > Um utilizador pode interagir com o sistema de duas formas: como jogador ou como árbitro. Os jogadores podem integrar várias equipas e devem ser capazes de indicar a posição que preferem jogar. Por sua vez, o papel de árbitro pode ser desempenhado por um utilizador com ou sem certificado. Jogadores não podem ser árbitros e vice-versa.

  > Para a gestão de equipas, o sistema permite criar equipas com um nome, manter um histórico dos jogos em que participaram e das conquistas, isto é, posições de pódio em campeonatos. É também necessário registar os jogadores que compõem cada equipa. As equipas não têm limite de tamanho, e cada jogador pode integrar várias equipas.

  > No que diz respeito aos jogos de futsal, deverá ser possível criar jogos amigáveis, sem associação a qualquer campeonato, bem como jogos que façam parte de um campeonato. A cada jogo associa-se uma data, horário, local, equipas e estatísticas relevantes. Os organizadores devem poder atribuir um ou mais árbitros aos jogos. Se houver mais de um árbitro, um deles deverá ser considerado o principal. Cada jogo tem duas equipas de cinco (5) jogadores, dos quais um será designado como guarda-redes.

  > A criação e organização de campeonatos envolve a definição de diferentes modalidades. Todos os campeonatos serão disputados por pontos, mas pretende-se estender (no futuro) o sistema para que seja suportada também a funcionalidade de campeonatos no formato de eliminatória. Um campeonato é disputado por um mínimo de 8 equipas e é necessária a presença de, pelo menos, um árbitro certificado nas partidas de campeonato.

  > Por fim, o registo de resultados e estatísticas deve permitir que, no final de cada jogo, se registe o resultado final (placar), a equipa vitoriosa, bem como quaisquer cartões atribuídos a jogadores. Em competições por pontos, o sistema atualiza a pontuação e a classificação das equipas. No formato de eliminatória, define-se quem avança para a fase seguinte.

</details>

## Interfaces
- **Interface nativa:** Utilizada para os casos de uso de autenticação & criação, atualização e remoção de entidades
- **Interface web:** Utilizada para o sistema de buscas com filtros & registo de resultados de jogos

## Execução
- **Backend** (necessário ter o Docker Desktop a correr): `bash SoccerNow/run.sh`
- **Acesso à interface nativa:** `mvn -f JavaFX/pom.xml javafx:run`
- **Acesso à interface web:** http://localhost:8080

## Tecnologias usadas
| Tecnologia       | Uso                                                                |
|------------------|--------------------------------------------------------------------|
| Java             | Linguagem de programação principal                                 |
| Spring Framework | Framework usada no backend da aplicação                            |
| Thymeleaf        | Motor de templates usado para o desenvolvimento da interface web   |
| JavaFX           | Plataforma usada para o desenvolvimento da interface nativa        |
| REST API         | API usada para a comunicação com o backend através de pedidos HTTP |
| PostgreSQL       | SGBD usado para a base de dados da aplicação                       |
| Docker           | Plataforma usada para o deployment da aplicação                    |
