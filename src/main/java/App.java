import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import java.util.List;
import java.util.Scanner;
import javax.ws.rs.core.MediaType;

public class App {
    private static final String URL = "http://localhost:8080/rest/job";

    private static List<Job> searchByArg(Client client, Job nArg) {
        WebResource webResource = client.resource(URL);

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
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed");
        }
        GenericType<List<Job>> type = new GenericType<List<Job>>() {
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

        Job newJob = new Job("Учитель", null, null, null, 5);
        //newJob = inputJob();
        printList(searchByArg(client, newJob));
    }
}
