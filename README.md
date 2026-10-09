# 🍃 Mini-Spring Boot

Ce projet consiste à développer un framework Java allégé (**Mini-Spring Boot**) de zéro, sans dépendance externe. L'objectif est de **démystifier la magie** derrière l'Inversion de Contrôle (IoC), l'Injection de Dépendances (DI) et l'Auto-configuration, qui propulsent la création de toutes les applications d'entreprise modernes.

---

## 🚀 Fonctionnalités clés

* **Conteneur IoC & Component Scanning** : Détection automatique des classes annotées (`@MiniComponent`, `@MiniRestController`) via la réflexion Java.
* **Injection de Dépendances (DI)** : Liaison automatique des services avec `@MiniAutowired`.
* **Serveur Web Embarqué** : Routage HTTP natif (`@MiniGetMapping`, `@MiniPostMapping`) et gestion des requêtes REST sans serveur externe.
* **Auto-Configuration** : Démarrage en une seule ligne de code avec `MiniSpringApplication.run(App.class)`.

## 🛠️ Technologies utilisées

* **Langage** : Java 21
* **Dépendances** : Aucune (100% Java natif / API Reflection)

## 💻 Exemple d'utilisation

Voici à quoi ressemble une application construite avec ce mini-framework :

```java
@MiniRestController
public class UserController {

    @MiniAutowired
    private UserService userService;

    @MiniGetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }
}

// Lancement de l'application
public class Application {
    public static void main(String[] args) {
        MiniSpringApplication.run(Application.class);
    }
}
```

## ⚙️ Installation et Exécution

1. **Cloner le projet** :
   ```bash
   git clone https://github.com
   cd mini-spring-boot
   ```

2. **Compiler le projet** :
   ```bash
   mvn clean package
   ```

3. **Lancer l'application de démonstration** :
   ```bash
   java -jar target/mini-spring-boot-1.0.jar
   ```

## 📈 Objectifs pédagogiques

* Comprendre le coût de performance et le fonctionnement de la réflexion en Java.
* Maîtriser le cycle de vie des Singletons et la gestion des contextes applicatifs.
* Tirer parti des fonctionnalités modernes de Java 21 pour optimiser le framework.
