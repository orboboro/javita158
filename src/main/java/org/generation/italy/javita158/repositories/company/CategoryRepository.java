package org.generation.italy.javita158.repositories.company;

import org.generation.italy.javita158.model.company.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/** Esercizi sulle categorie. Leggere prima package-info.java; scrivere un metodo per TODO. */
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // TODO CA1 [Livello 1 - DERIVATA]
    // Cercare le categorie con categoryName uguale al parametro String name.
    // Restituire List<Category>: il mapping non dichiara il nome univoco.
    // Controllare anche un nome inesistente: il risultato deve essere una lista vuota.

    // TODO CA2 [Livello 2 - DERIVATA]
    // Cercare le categorie la cui description contiene String text, ignorando il caso.
    // Restituire List<Category>, ordinata per categoryName crescente e poi id crescente.

    // TODO CA3 [Livello 3 - JPQL]
    // Riscrivere CA1 con @Query e un parametro nominato collegato da @Param.
    // Usare il nome Java Category e l'attributo categoryName. Confrontare gli id con CA1.

    // TODO CA4 [Livello 5 - JPQL, NOT EXISTS]
    // Restituire List<Category> delle categorie senza alcun prodotto, in ordine di id.
    // Category non contiene una collezione products: partire da Product nella sottoquery.
}
