package org.generation.italy.javita158.repositories.company.projections;

/** DTO del laboratorio: usare SELECT NEW con il nome completo della classe.
 * Il costruttore riceve i valori nello stesso ordine della SELECT; non è un’entity. */
public final class EmployeeManagerView {

    private final Integer employeeId;
    private final String lastName;
    private final String managerLastName;

    public EmployeeManagerView(Integer employeeId, String lastName, String managerLastName) {
        this.employeeId = employeeId;
        this.lastName = lastName;
        this.managerLastName = managerLastName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getManagerLastName() {
        return managerLastName;
    }
}
