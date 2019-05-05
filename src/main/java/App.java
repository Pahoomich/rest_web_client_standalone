import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final String URL = "http://localhost:8080/rest/job";

    private static List<Job> searchByArg(Client client, Job nArg) {
        WebResource webResource = client.resource(URL + "/search");

        if (nArg.getJob_title() != null) {
            webResource = webResource.queryParam("name", nArg.getJob_title());
        }
        if (nArg.getMin_salary() != null) {
            webResource = webResource.queryParam("min_salary", String.valueOf(nArg.getMin_salary()));
        }
        if (nArg.getMax_salary() != null) {
            webResource = webResource.queryParam("max_salary", String.valueOf(nArg.getMax_salary()));
        }
        if (nArg.getJob_language() != null) {
            webResource = webResource.queryParam("job_language", nArg.getJob_language());
        }
        if (nArg.getWork_exp() != null) {
            webResource = webResource.queryParam("work_exp", String.valueOf(nArg.getWork_exp()));
        }

        ClientResponse response =
                webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        GenericType<List<Job>> type = new GenericType<List<Job>>() {
        };
        return response.getEntity(type);
    }

    private static String addNew(Client client, Job nJob) {
        WebResource webResource = client.resource(URL + "/addNew");
        ClientResponse response =
                webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, nJob);
        GenericType<String> type = new GenericType<String>() {
        };
        return response.getEntity(type);
    }

    private static String updateRecord(Client client, Integer id, Job nJob) {
        String URLUpdate = URL + "/" + id.toString();
        WebResource webResource = client.resource(URLUpdate);
        ClientResponse response =
                webResource.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, nJob);
        GenericType<String> type = new GenericType<String>() {
        };
        return response.getEntity(type);
    }


    private static String dropRecord(Client client, Integer id) {
        String URLUpdate = URL + "/" + id.toString();
        WebResource webResource = client.resource(URLUpdate);
        ClientResponse response =
                webResource.accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
        GenericType<String> type = new GenericType<String>() {
        };
        return response.getEntity(type);
    }


    private static void printList(List<Job> jobs) {
        for (Job job : jobs) {
            System.out.println(job);
        }
    }

    private static Job inputJob() {

        Job newJob = new Job();
        String input = null;
        Scanner in = new Scanner(System.in);

        newJob.job_title = null;
        System.out.print("Imput a title of work (exm: Уборщик, Учитель, Директор): ");
        input = in.nextLine();
        if (input.length() != 0) {
            newJob.job_title = input;
        }

        newJob.min_salary = null;
        System.out.print("Imput a min salary (exm: 5000, 7000): ");
        input = in.nextLine();
        if (input.length() != 0) {
            newJob.min_salary = Integer.valueOf(input);
        }

        newJob.max_salary = null;
        System.out.print("Imput a maxSalary (exm: 15000, 20000, 25000): ");
        input = in.nextLine();
        if (input.length() != 0) {
            newJob.max_salary = Integer.valueOf(input);
        }

        newJob.job_language = null;
        System.out.print("Imput a job language (exm: русский, английский): ");
        input = in.nextLine();
        if (input.length() != 0) {
            newJob.job_language = input;
        }

        newJob.work_exp = null;
        System.out.print("Imput a work experience (exm: 1, 0, 5): ");
        input = in.nextLine();
        if (input.length() != 0) {
            newJob.work_exp = Integer.valueOf(input);
        }

        in.close();
        return newJob;
    }

    public static void main(String[] args) {

        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("labuser", "12345678"));

        //Job newJob = new Job("Охранник", 10, 10, "русский", 5);
        //newJob = inputJob();
        //printList(searchByArg(client, newJob));
        //String massege = updateRecord(client, 5, newJob);
        //String massege = addNew(client, newJob);
        //int outMethod = addNew(client,newJob);
        //System.out.println(massege);

        Job newJob = new Job();

        Scanner in = new Scanner(System.in);
        System.out.println("Введите номер действия:");
        System.out.println("1. Поиск работы по критериям");
        System.out.println("2. Добавть новую работу");
        System.out.println("3. Изменить существующую запись");
        System.out.println("4. Удалить запись");
        System.out.print("Ваш выбор: ");
        int choice = in.nextInt();

        String massege = "";
        int id;
        switch (choice) {
            case (1):
                newJob = inputJob();
                List<Job> jobs = searchByArg(client, newJob);
                for (Job job : jobs) {
                    System.out.println("title: " + job.getJob_title() +
                            ", min salary: " + job.getMin_salary() +
                            ", max salary: " + job.getMax_salary() +
                            ", job language: " + job.getJob_language() +
                            ", work experience: " + job.getWork_exp());
                }
                System.out.println("Total jobs: " + jobs.size());
                break;
            case (2):
                newJob = inputJob();
                massege = addNew(client, newJob);
                System.out.println("  Id Записи" + massege + " (успешно добавлена)");
                break;
            case (3):
                System.out.print("Введите id записи, которую нужно изменить: ");
                id = in.nextInt();
                System.out.println("Введите новые данные");
                newJob = inputJob();
                massege = updateRecord(client, id, newJob);
                System.out.println("Изменение записи с id " + id + " прошло со статусом - " + massege);
                break;
            case (4):
                System.out.print("Введите id записи, которую нужно удалить: ");
                id = in.nextInt();
                massege = dropRecord(client,id);
                System.out.println("Удаление записи с id " + id + " прошло со статусом - " + massege);
                break;
        }
    }
}
