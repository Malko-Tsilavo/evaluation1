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

Existing app

Creation de la table budget

Creation de la table depense

Creation de la table taux_alerte

Creation de l'entite Budget

Creation du controller BudgetController

Creation de la fonction showAllBudget

Creation de la fonction showBudgetInsert

Creation de la fonction insertBudget

Creation de la fonction deleteBudget

Creation de la fonction updateBudget

Creation de la page show-budget

Creation de la page insert-budget

Creation de l'entite TauxAlert

Creation du controller TauxAlertController

Creation de la fonction showAllTauxAlert

Creation de la fonction showTauxAlertInsert

Creation de la fonction insertTauxAlert

Creation de la fonction deleteTauxAlert

Creation de la fonction updateTauxAlert

Creation de la page show-TauxAlert

Creation de la page insert-TauxAlert

Creation de l'entite Depense

Creation du controller DepenseController

Creation de la fonction showAllDepense

Creation de la fonction showDepenseInsert

Creation de la fonction insertDepense

Creation de la fonction deleteDepense

Creation de la fonction updateDepense

Creation de la page show-depense

Creation de la page insert-depense

Creation de la fonction de notification
Détails: Active une notification si le budget atteint seuil
Active aussi si le budget est dépasser

New App
Configuration de SecurityConfig dans existing app
Detail: Configurer securityConfig pour qu'elle autorise l'utilisation
des url de l'api rest

Creation du controller AuthentificationRestController
Détail: Controller charger de l'envoie et de la recepetion des
données du login

