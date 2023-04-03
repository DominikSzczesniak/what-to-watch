package pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class InFileUserRepository implements UserRepository {

    private final String fileName;
    private final int INDEX_WITH_ID_NUMBER_IN_CSV = 1;
    private final int INDEX_WITH_USERNAME_IN_CSV = 0;


    @Override
    public UserId createUser(final String username) {
        createFile();
        if (!userHasId(username)) {
            UserId userId = new UserId(nextUserId());
            try (FileWriter fw = new FileWriter(fileName, true)) {
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(username + "," + (userId.getId() + 1));
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return getExistingUserId(username);
        }
        return getExistingUserId(username);
    }

    @Override
    public int nextUserId() {
        String lastLine = "";
        int id = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lastLine = line;
                List<String> listLine = Arrays.stream(lastLine.split("[,]")).toList();
                id = Integer.parseInt(listLine.get(INDEX_WITH_ID_NUMBER_IN_CSV));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    @Override
    public List<User> findAll() {
        List<User> listLine = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> stringLine = Arrays.stream(line.split("[,]")).toList();
                listLine.add(new User(stringLine.get(0) ,new UserId(Integer.parseInt(stringLine.get(INDEX_WITH_ID_NUMBER_IN_CSV)))));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listLine;
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

    private UserId getExistingUserId(final String username) {
        UserId id = new UserId(0);
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> listLine = Arrays.stream(line.split("[,]")).toList();
                if (listLine.get(0).equals(username)) {
                    id = new UserId(Integer.parseInt(listLine.get(INDEX_WITH_ID_NUMBER_IN_CSV)));
                    return id;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    private boolean userHasId(final String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> listLine = Arrays.stream(line.split("[,]")).toList();
                if (listLine.get(INDEX_WITH_USERNAME_IN_CSV).equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


}

