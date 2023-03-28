package pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class InFileUserRepository implements UserRepository {

    final String fileName;

    public InFileUserRepository(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    public UserId getUserId(final String username) {
        createFile();
        createUser(username);
        return new UserId(getExistingUserId(username));
        }

    @Override
    public void createUser(final String username) {
        if (!userHasId(username)) {
            int userId = nextUserId();
            try {
                FileWriter fw = new FileWriter(fileName, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(username + "," + (userId + 1));
                bw.newLine();
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int nextUserId() {
        String lastLine = "";
        int id = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                lastLine = line;
                List<String> listLine = Arrays.stream(lastLine.split("[,]")).toList();
                id = Integer.parseInt(listLine.get(1));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    @Override
    public List<UserId> findAll() {
        return null; // TODO: dodać implementację
    }


    private void createFile() {
        try {
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private int getExistingUserId(final String username) {
        int id = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                List<String> listLine = Arrays.stream(line.split("[,]")).toList();
                if (listLine.get(0).equals(username)) {
                    id = Integer.parseInt(listLine.get(1));
                    return id;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    private boolean userHasId(final String username) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                List<String> listLine = Arrays.stream(line.split("[,]")).toList();
                if (listLine.get(0).equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


}

