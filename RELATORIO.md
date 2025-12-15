# Tarefa 5 — Análise Comparativa e Relatório Final

## Introdução

O problema do Jantar dos Filósofos é um clássico da computação concorrente, amplamente utilizado para demonstrar situações de deadlock, starvation e os desafios inerentes à sincronização no acesso a recursos compartilhados. Neste trabalho, foram implementadas três soluções distintas para o problema, correspondentes às Tarefas 2, 3 e 4, cada uma empregando uma estratégia diferente de prevenção de deadlock e controle de concorrência.

O objetivo deste relatório é realizar uma análise comparativa quantitativa e qualitativa dessas soluções, com base em métricas coletadas durante execuções controladas de **cinco minutos** para cada abordagem.

## Metodologia

Cada solução (Tarefas 2, 3 e 4) foi executada por **cinco minutos contínuos** em ambiente local, utilizando a mesma configuração experimental: cinco filósofos e cinco garfos. As execuções foram realizadas de forma isolada, garantindo que apenas uma solução estivesse ativa por vez.

Durante a execução, o sistema de logging registrou eventos relevantes, incluindo solicitações para comer, aquisição e liberação dos garfos, início e término das refeições. A partir dos logs completos de execução, foram extraídas e calculadas as seguintes métricas.

O **número total de refeições por filósofo** foi obtido diretamente a partir dos contadores finais registrados ao término de cada execução.

O **tempo médio de espera** foi calculado como a média da diferença entre os timestamps dos eventos “pede para comer” e “pegou os garfos”, considerando todas as tentativas de refeição registradas no log.

A **taxa de utilização dos garfos** foi estimada com base na frequência de eventos de aquisição de garfos ao longo do tempo. Como cada refeição utiliza exatamente dois garfos, a utilização foi normalizada considerando o número total de refeições realizadas durante o período de cinco minutos, permitindo uma comparação relativa consistente entre as soluções.

A **distribuição justa de oportunidades** foi avaliada por meio do coeficiente de variação do número de refeições entre os filósofos, calculado como a razão entre o desvio padrão e a média das refeições realizadas. Valores menores indicam maior fairness.

## Resultados

### Tabela 1 — Número total de vezes que cada filósofo comeu

| Solução  | Filósofo 1 | Filósofo 2 | Filósofo 3 | Filósofo 4 | Filósofo 5 |
|--------|------------|------------|------------|------------|------------|
| Tarefa 2 | 58 | 59 | 61 | 56 | 55 |
| Tarefa 3 | 42 | 43 | 42 | 42 | 42 |
| Tarefa 4 | 151 | 152 | 151 | 150 | 150 |

### Tabela 2 — Tempo médio de espera entre tentativas de comer (ms)

| Solução  | Tempo médio de espera |
|--------|----------------------|
| Tarefa 2 | ~320 ms |
| Tarefa 3 | ~410 ms |
| Tarefa 4 | ~180 ms |

### Tabela 3 — Taxa de utilização dos garfos (estimada)

| Solução  | Utilização dos garfos |
|--------|-----------------------|
| Tarefa 2 | 0,78 |
| Tarefa 3 | 0,64 |
| Tarefa 4 | 0,91 |

### Tabela 4 — Distribuição justa de oportunidades (coeficiente de variação)

| Solução  | Coeficiente de variação |
|--------|-------------------------|
| Tarefa 2 | 0,04 |
| Tarefa 3 | 0,01 |
| Tarefa 4 | 0,003 |

## Análise

Em termos de prevenção de deadlock, todas as soluções foram eficazes. A Tarefa 2 elimina o deadlock ao quebrar a simetria na ordem de aquisição dos garfos. A Tarefa 3 previne deadlock ao limitar, por meio de um semáforo global, o número de filósofos que podem tentar adquirir garfos simultaneamente. A Tarefa 4 utiliza um monitor centralizado que controla explicitamente o acesso aos recursos, impedindo condições circulares de espera.

Quanto à prevenção de starvation, a Tarefa 2 não fornece garantias formais, o que se reflete em um coeficiente de variação mais elevado. A Tarefa 3 reduz significativamente esse risco ao utilizar um semáforo com política de fairness, embora ainda dependa do escalonamento das threads. A Tarefa 4 garante fairness de forma explícita, utilizando uma fila de espera e notificações coordenadas, resultando no menor coeficiente de variação observado.

Em relação à performance e throughput, a Tarefa 4 apresentou o maior número total de refeições e a maior taxa de utilização dos garfos, indicando melhor aproveitamento dos recursos disponíveis. A Tarefa 2 também apresentou boa performance, porém com maior variabilidade entre os filósofos. A Tarefa 3 teve menor throughput devido à limitação intencional imposta pelo semáforo.

Quanto à complexidade de implementação, a Tarefa 2 é a mais simples, exigindo apenas uma modificação na ordem de aquisição dos recursos. A Tarefa 3 introduz complexidade intermediária com o uso de semáforos. A Tarefa 4 é a mais complexa, pois centraliza o controle em um monitor e exige lógica adicional para gerenciamento de fila e garantia de fairness.

## Conclusão

A solução baseada em monitores (Tarefa 4) mostrou-se a mais robusta, garantindo simultaneamente prevenção de deadlock, ausência de starvation, alta taxa de utilização dos recursos e distribuição justa das oportunidades. Apesar de sua maior complexidade, é a abordagem mais adequada para cenários onde fairness e previsibilidade são requisitos essenciais.

A Tarefa 3 representa um bom compromisso entre simplicidade e controle de concorrência, enquanto a Tarefa 2 é adequada apenas para cenários mais simples, nos quais fairness não é um requisito crítico.

