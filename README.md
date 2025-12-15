# PROVA PGPD — PROJETO JAVA  
## NetBeans | Maven

---

## 1. REQUISITOS

- Java (JDK): versão utilizada no NetBeans (ex.: JDK 17)
- Apache NetBeans (IDE)
- Maven (embutido no NetBeans)

---

## 2. ESTRUTURA DO PROJETO

prova/
├── pom.xml # Configuração do Maven
├── nbactions.xml # Ações de execução do NetBeans
├── src/
│ └── main/
│ └── java/
│ ├── tarefa1/ # Implementação da Tarefa 1
│ ├── tarefa2/ # Implementação da Tarefa 2
│ ├── tarefa3/ # Implementação da Tarefa 3
│ └── tarefa4/ # Implementação da Tarefa 4
└── target/ # Gerado automaticamente pelo Maven (não versionado)

---

## 3. COMPILAÇÃO DO PROJETO

A compilação do projeto é realizada automaticamente pelo NetBeans.

### Compilação manual
1. Abra o projeto no NetBeans  
2. Clique com o botão direito no projeto  
3. Selecione a opção **Build**

O NetBeans utiliza o Maven para compilar o código-fonte.

---

## 4. EXECUÇÃO DO PROJETO

A execução do projeto é feita exclusivamente pelo NetBeans.

### Executar uma tarefa específica
1. No NetBeans, navegue até `src/main/java`  
2. Abra o pacote da tarefa desejada (`tarefa1`, `tarefa2`, etc.)  
3. Clique com o botão direito na classe `Main`  
4. Selecione a opção **Run File**

Cada tarefa possui sua própria classe `Main`, conforme solicitado no enunciado da prova.

