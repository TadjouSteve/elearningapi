pipeline {
    agent any

    tools {
        // Indique à Jenkins d'utiliser le JDK installé
        // Assurez-vous d'avoir configuré le JDK dans "Manage Jenkins" -> "Global Tool Configuration"
        jdk 'JDK_17' 
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building the Spring Boot application...'
                // Commande pour compiler l'application et générer le .jar avec Maven ou Gradle
                // Si vous utilisez Maven
                sh 'mvn clean package -DskipTests'
                // Si vous utilisez Gradle
                // sh './gradlew build'
            }
        }

        stage('Run') {
            steps {
                echo 'Stopping previous instance...'
                // Tente d'arrêter l'instance précédente pour éviter les erreurs de port
                sh 'pkill -f streengeAPI'

                echo 'Starting the Spring Boot application...'
                // Lance le fichier .jar en tâche de fond
                sh 'java -jar target/*.jar &' 
            }
        }
    }
}