<!DOCTYPE html>
<html lang="fr">
<head>
<#include "header.ftl">
</head>

<body>


<div class="container">
    <div class="alert alert-info text-center" role="alert">
        Service qui propose de renvoyer des informations au format <span style="font-family: 'Courier New'">JSON</span> sur les arbres de France et d'Europe à partir du site <a href="http://www.snv.jussieu.fr/bmedia/arbres/">Arbres et arbustes
        une encyclopédie en images</a>
    </div>
    <hr>
    <p>Message: ${message}</p>
    <hr>
    <div class="row">
        <h3><span class="glyphicon glyphicon-info-sign"></span> Les requêtes possibles</h3>
        <ul>
            <li><a href="/arbres">La liste des arbres</a></li>
            <li><a href="/categories">Les catégories</a></li>
            <li>Les arbres d'une catégorie par exemple pour les arbres avec des feuilles découpées<pre>/categorie/Feuilles Découpées</pre></li>
        </ul>
    </div>


</body>
</html>
