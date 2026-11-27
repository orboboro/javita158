package org.generation.italy.javita158.repositories.company;

import org.generation.italy.javita158.model.company.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Esercizi sulle categorie. Leggere prima package-info.java; scrivere un metodo per TODO. */
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // CA1 RISOLTO [Livello 1 - DERIVATA]
    // Cercare le categorie con categoryName uguale al parametro String name.
    // Restituire List<Category>: il mapping non dichiara il nome univoco.
    // Controllare anche un nome inesistente: il risultato deve essere una lista vuota.
    // Spring interpreta CategoryName come l'attributo Java categoryName e usa l'uguaglianza.
    // Non servono @Query né un'implementazione manuale. Vedere CategoryRepositoryTest.
    List<Category> findByCategoryName(String name);

    // TODO CA2 [Livello 2 - DERIVATA]
    // Cercare le categorie la cui description contiene String text, ignorando il caso.
    // Restituire List<Category>, ordinata per categoryName crescente e poi id crescente.
    List<Category> findByDescriptionContainingIgnoreCaseOrderByCategoryNameAscIdAsc(String text);

    // TODO CA3 [Livello 3 - JPQL]
    // Riscrivere CA1 con @Query e un parametro nominato collegato da @Param.
    // Usare il nome Java Category e l'attributo categoryName. Confrontare gli id con CA1.

    // TODO CA4 [Livello 5 - JPQL, NOT EXISTS]
    // Restituire List<Category> delle categorie senza alcun prodotto, in ordine di id.
    // Category non contiene una collezione products: partire da Product nella sottoquery.
}
