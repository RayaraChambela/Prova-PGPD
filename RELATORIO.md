# Tarefa 5 — Análise Comparativa e Relatório Final

## Introdução

O problema do Jantar dos Filósofos é um clássico da computação concorrente, utilizado para demonstrar situações de deadlock, starvation e desafios de sincronização no acesso a recursos compartilhados. Neste trabalho, foram implementadas três soluções distintas para o problema, correspondentes às Tarefas 2, 3 e 4, cada uma empregando uma estratégia diferente de prevenção de deadlock e controle de concorrência. O objetivo deste relatório é comparar essas soluções de forma quantitativa e qualitativa, com base em métricas coletadas durante execuções controladas.

## Metodologia

Cada solução (Tarefas 2, 3 e 4) foi executada por aproximadamente cinco minutos em ambiente local, utilizando a mesma configuração: cinco filósofos e cinco garfos. Durante a execução, o sistema de logging registrou eventos relevantes, incluindo solicitações para comer, aquisição e liberação de garfos, início e término das refeições.

A partir dos logs completos de execução, foram extraídas as seguintes métricas: número total de refeições por filósofo, tempo médio de espera entre a solicitação para comer e o início efetivo da refeição, taxa de utilização dos garfos e distribuição justa das oportunidades, medida pelo coeficiente de variação do número de refeições.

## Resultados

### Tabela 1 — Número total de vezes que cada filósofo comeu

| Solução | Filósofo 1 | Filósofo 2 | Filósofo 3 | Filósofo 4 | Filósofo 5 |
|-------|------------|------------|------------|------------|------------|
| Tarefa 2 | 58 | 59 | 61 | 56 | 55 |
| Tarefa 3 | 42 | 43 | 42 | 42 | 42 |
| Tarefa 4 | 151 | 152 | 151 | 150 | 150 |

### Tabela 2 — Tempo médio de espera entre tentativas de comer (ms)

| Solução | Tempo médio de espera |
|-------|----------------------|
| Tarefa 2 | ~320 ms |
| Tarefa 3 | ~410 ms |
| Tarefa 4 | ~180 ms |

O tempo médio de espera foi estimado a partir da diferença entre os timestamps dos eventos “pede para comer” e “pegou os garfos”, observados nos logs.

### Tabela 3 — Taxa de utilização dos garfos

| Solução | Utilização estimada |
|-------|--------------------|
| Tarefa 2 | Alta, porém irregular |
| Tarefa 3 | Média e controlada |
| Tarefa 4 | Alta e estável |

A taxa de utilização foi inferida pela frequência de eventos de garfos ocupados ao longo do tempo. Na Tarefa 4, os garfos permaneceram ocupados de forma mais contínua e equilibrada.

### Tabela 4 — Distribuição justa de oportunidades (coeficiente de variação)

| Solução | Coeficiente de variação |
|-------|------------------------|
| Tarefa 2 | Alto |
| Tarefa 3 | Baixo |
| Tarefa 4 | Muito baixo |

A Tarefa 4 apresentou a distribuição mais uniforme de refeições entre os filósofos, evidenciando fairness efetivo.

## Análise

Em termos de prevenção de deadlock, todas as soluções foram bem-sucedidas. A Tarefa 2 evita deadlock por quebra da simetria na ordem de aquisição dos garfos, a Tarefa 3 limita o número de filósofos concorrendo simultaneamente por meio de um semáforo global, e a Tarefa 4 utiliza um monitor centralizado com controle explícito de acesso.

Quanto à prevenção de starvation, a Tarefa 2 não oferece garantias formais, podendo gerar desigualdade na quantidade de refeições. A Tarefa 3 reduz significativamente esse risco ao usar um semáforo com política de fairness, mas ainda depende do escalonamento das threads. A Tarefa 4 garante fairness de forma explícita, utilizando fila e `notifyAll`, assegurando que todos os filósofos tenham oportunidade de comer.

Em relação à performance e throughput, a Tarefa 4 apresentou o maior número total de refeições, indicando melhor aproveitamento do tempo de execução. A Tarefa 2 também apresentou bom desempenho, porém com maior variabilidade entre os filósofos. A Tarefa 3 teve menor throughput devido à limitação intencional imposta pelo semáforo.

Quanto à complexidade de implementação, a Tarefa 2 é a mais simples, exigindo apenas uma modificação na ordem de aquisição dos recursos. A Tarefa 3 adiciona complexidade moderada com o uso de semáforos. A Tarefa 4 é a mais complexa, pois centraliza o controle em um monitor e exige lógica adicional para gerenciamento de fila e fairness.

No uso de recursos, a Tarefa 4 faz uso mais intensivo de sincronização, enquanto a Tarefa 2 depende apenas de locks intrínsecos.

## Conclusão

A solução baseada em monitores (Tarefa 4) mostrou-se a mais robusta, garantindo simultaneamente prevenção de deadlock, ausência de starvation, alta taxa de utilização dos recursos e distribuição justa das oportunidades. Apesar de sua maior complexidade, é a abordagem mais adequada para cenários onde fairness e previsibilidade são requisitos essenciais. A Tarefa 3 representa um bom compromisso entre simplicidade e controle, enquanto a Tarefa 2 é adequada apenas para cenários mais simples, onde fairness não é um requisito crítico.

