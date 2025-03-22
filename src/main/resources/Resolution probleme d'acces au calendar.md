### Resolution probleme d'acces au calendar
- Entrer dans google console
- oath permission
- audience
- ajouter l'email


### Resolution weather api
- Entrer dans le site https://www.weatherapi.com/
- Créez un compte (si ce n'est pas déja fait)
- Connectez vous avec votre email et mdp

Lead: Un lead est simplement une personne ou une entreprise qui a montré de l'intérêt pour vos produits ou services, sans forcément être prête à acheter immédiatement.

Create Leads: par rapport au fichier à  joindre le fichier acceptable ne sont que des fichiers sheet,docs
Delete template:java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "emailSettings" is null

Calendar: Lorsque nous modifions un evenement et que l'on fait toute la journee il y a un probleme car de base 
elle recupère une date debut et date fin mais si on definit que c'est toute la journee dans le telephone sur la base 
aucune date n'est precisé

my-lead:Arrangement du thymeleaf <td><a th:href="${home} + 'customer/lead/' + ${lead.leadId}"><i class="fas fa-eye"></i></a></td>
qui était comme ceci <td><a th:href="${home + 'customer/lead/' + ${lead.leadId}"><i class="fas fa-eye"></i></a></td>