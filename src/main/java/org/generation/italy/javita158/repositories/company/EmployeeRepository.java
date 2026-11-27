package org.generation.italy.javita158.repositories.company;

import org.generation.italy.javita158.model.company.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/** Date e relazione ricorsiva: Employee.manager è un altro Employee. */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // TODO EM1 [Livello 1 - DERIVATA]
    // Dato String lastName, trovare i dipendenti con quel cognome: List<Employee>.
    // Il cognome non identifica una sola persona: non restituire Optional<Employee>.
    List<Employee> findByLastName(String lastName);

    // TODO EM2 [Livello 1 - DERIVATA]
    // Restituire List<Employee> dei dipendenti senza manager. Nessun parametro.
    List<Employee> findByManagerNull();

    // TODO EM3 [Livello 2 - DERIVATA]
    // Dato LocalDateTime cutoff, trovare i dipendenti assunti PRIMA di cutoff.
    // Restituire List<Employee>, ordinata per hireDate crescente e poi id.
    // Un'assunzione esattamente a cutoff non deve essere inclusa.
    List<Employee> findByHireDateLessThanOrderByHireDateAscIdAsc(LocalDateTime cutoff);

    // TODO EM4 [Livello 3 - DERIVATA + JPQL, DUE METODI]
    // Dato Integer managerId, trovare i subordinati DIRETTI di quel responsabile.
    // Restituire List<Employee> nelle due versioni. Non cercare l'intera gerarchia.
    // Escludere i dipendenti senza manager; usare l'id, non il cognome del responsabile.
    List<Employee> findByManagerId(Integer managerId);

    @Query("select e from employee e where e.manager.id = :managerId")
    List<Employee> findByManagerIdJpql(@Param("managerId") Integer managerId);

    // TODO EM5 [Livello 3 - JPQL, LEFT JOIN + DTO]
    // Restituire List<EmployeeManagerView> con employeeId, lastName e managerLastName.
    // Includere TUTTI i dipendenti: managerLastName sarà null per chi non ha manager.
    // Usare SELECT NEW con la classe fornita e ordinare per employeeId crescente.


    // TODO EM6 [Livello 5 - JPQL, SOTTOQUERY CORRELATA]
    // Restituire List<Employee> che hanno gestito più di long minimumOrders ordini.
    // minimumOrders >= 0; confronto strettamente maggiore; ordinamento per id.
    // Contare gli Order il cui employee è il dipendente della query esterna.
}
