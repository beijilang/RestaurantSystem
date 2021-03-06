package backend.employee;

import java.util.ArrayList;

/** An EmployeeManager class. This class manages all the employees working in the restaurant. */
public class EmployeeManager {
  // ArrayList of all employees.
  private static ArrayList<Employee> employeeList = new ArrayList<>();

  /**
   * An instance of backend.employee is added to backend.employee manager.
   *
   * @param employee The backend.employee instance that is added to this backend.employee manager.
   */
  public static void add(Employee employee) {
    employeeList.add(employee);
  }

  /**
   * Returns a string representation of all the employees.
   *
   * @return a string representation of all the employees.
   */
  public static String allEmployeesToString() {
    StringBuilder result = new StringBuilder("List of all employees: \n");
    result.append("==================================");
    for (Employee e : employeeList) {
      result.append(e.toString()).append("\n");
    }
    result.append("==================================");

    return result.toString();
  }

  /**
   * Return an backend.employee whose id is equal to <code>id</code>.
   *
   * @param id Id of the backend.employee this method returns.
   * @return An Employee instance whose id is equal to <code>id</code>.
   */
  public static Employee getEmployeeById(int id) {
    if (id < 1 || id > employeeList.size()) {
      System.err.println("Not a valid backend.employee id.");
      (new Exception()).printStackTrace();
      return null;
    } else {
      return employeeList.get(id - 1);
    }
  }
}
