# API_Tech_Test

Ce projet est une API simple pour enregistrer et afficher les détails des utilisateurs. Il utilise une base de données H2 intégrée et implémente la journalisation AOP pour suivre les appels et les temps de traitement.

## Endpoints

### Enregistrement d'un utilisateur

Enregistrez un utilisateur avec les détails suivants :

- Nom d'utilisateur
- Date de naissance
- Pays de résidence
- Numéro de téléphone (optionnel)
- Sexe (optionnel)

**URL :** `/createuser 

**Méthode :** POST

Exemple d'appel avec des paramètres en URL :

**POST /createuser?username=Michel&birthdate=2000-01-01&country=France&phone=1122334455&gender=M**

Les paramètres en URL doivent être transmis avec le point d'interrogation ? suivi des noms de paramètres et de leurs valeurs, séparés par des &.

- Dans le cas d'un enregistrement en base de données en succès, un message `"New user created"` est retourné ainsi que le code Http 201 (Created)

- Dans le cas où les données de l'utilisateur à enregistrer montrent que celui ci n'est pas désigné comme résident en `France`ou que celu-ci ne serait pas majeur (date de naissance inférieure à date du jour - 18 ans), un message `"User creation is allowed only for adults from France."` est retourné ainsi que le code Http 400 (Bad Request)

- Dans le cas où les paramètres seraient erronés ou que le format de la date ne serait pas respecté, un message `"Information is wrong or not in the correct format (use yyyy-MM-dd for birthdate)"` est retourné ainsi que le code Http 400 (Bad Request)

### Affichage des détails d'un utilisateur

Récupérez les détails d'un utilisateur enregistré en spécifiant son ID.

**URL :** `/getuser`

**Méthode :** GET

Exemple d'appel en utilisant un paramètre d'URL :

**GET /getuser?id=1**

Les paramètres en URL doivent être transmis avec le point d'interrogation ? suivi des noms de paramètres et de leurs valeurs

**EXEMPLE DE REPONSE**

Dans le cas où un utilisateur existe avec l'id fourni, un code Http 200 (OK) est retrourné aunsi qu'une réponse JSON semblable à celle ci :

`{
    "id": 1,
    "username": "Michel",
    "birthdate": "2000-01-01",
    "country": "France",
    "phone": "1122334455",
    "gender": "M"
}`

Dans cet exemple, nous renvoyons un objet JSON qui représente les détails d'un utilisateur avec les champs suivants :

- id: L'identifiant de l'utilisateur.
- username: Le nom d'utilisateur de l'utilisateur.
- birthdate: La date de naissance de l'utilisateur.
- country: Le pays de résidence de l'utilisateur.
- phone: Le numéro de téléphone de l'utilisateur (optionnel).
- gender: Le sexe de l'utilisateur (optionnel).

Dans le cas où aucun utilisateur correspondant à l'id fourni n'est trouvé, un code Http 404 (Not found) est retourné.

# Installation du projet #

## Prérequis ##

- [JDK (Java Development Kit)](https://www.oracle.com/java/technologies/javase-downloads.html) installé (Java 8 ou supérieur)
- [Maven](https://maven.apache.org/download.cgi) installé (pour la gestion des dépendances)
- Un gestionnaire de versions Git, tel que [Git](https://git-scm.com/downloads), installé
- [Postman](https://www.postman.com/downloads/) installé 

## Étapes

### 1. Cloner le Projet

Ouvrez votre terminal et exécutez la commande suivante pour cloner le projet depuis le référentiel GitHub :

```bash
git clone https://github.com/MorganGer/API_Tech_Test.git
```

### 2.1 Sans IDE

#### Accéder au répertoire projet

Accédez au répertoire du projet que vous venez de cloner en utilisant la commande cd :

```bash
cd API_Tech_Test
```

#### Compiler le projet

Exécutez la commande Maven suivante pour compiler le projet et générer le fichier JAR

```bash
mvn clean install
```

#### Exécuter le projet

Pour exécuter l'application, utilisez la commande suivante :

```bash
java -jar target/votre-projet-1.0.0.jar
```

### 2.2 Avec IDE (Exemple avec IntelliJ IDEA)

#### Ouvrir le projet sur votre environnement de développement.

![image](https://github.com/MorganGer/API_Tech_Test/assets/133863039/9bff8eb7-c507-48a1-9ea8-db91e3cdc3e8)

#### Compiler le projet

![image](https://github.com/MorganGer/API_Tech_Test/assets/133863039/3fb2fafc-2e49-46e1-8849-e2fc4ee55267)

#### Exécuter le projet

![image](https://github.com/MorganGer/API_Tech_Test/assets/133863039/5ad6a599-b633-4668-bc2a-4215c3a8969f)

### 3. Tester l'API

Pour tester l'API nous utiliseront Postman (utiliser des commandes curl est aussi une possibilité).

#### Ouvrir Postman et importer une nouvelle collection

![image](https://github.com/MorganGer/API_Tech_Test/assets/133863039/b52c8819-ba42-4a68-99ad-18c945aea7f3)

![image](https://github.com/MorganGer/API_Tech_Test/assets/133863039/fd4c45a0-e8a5-4e61-8c66-38aa29b2eb43)

#### Exécuter les requêtes 

Si le projet est en cours d'exécution, les requêtes peuvent être exécutées.

Exemple : CreateUser
- Renseigner les paramètres souhaités
- Cliquer sur "Send"
- Observer la reponse (résultat)

Une fois notre premier utilisateur créé, nous pouvons lancer la requête GetUser sur l'id "1" pour en récupérer les informations. 

## Branches et pistes d'amélioration

Vous aurez probablement remarqué une troisième requête "CreateUserTestJson" dans la collection Postman.

La raison est que n'étant pas satisfait de la requête CreateUser initiale, j'ai souhaité me débarasser des paramètres en URL pour fournir en entrée de l'API un objet Utilisateur Json entier.

Cette requête est testable en suivant les indications suivantes :

- Ouvrir une console à la racine du projet et taper la commande suivante : 
```bash
git checkout test_json
```
- Alternativement, il est possible de changer de branche directement depuis l'IDE.
- Lancer le projet depuis l'IDE
- Utiliser la Requête CreateUserTestJson qui prend en entrée un body composé d'un objet User.

Cependant, les tests ne sont plus fonctionnels si cette méthode d'appel est utilisée et je ne suis pas encore parvenu à les faire fonctionner.
