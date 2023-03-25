package pl.szczesniak.dominik.whattowatch.users.domain.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class InFileUsersRepository implements UserRepository {

    final String fileName;

    @Override
    public int saveUserId(final User user) {
        createFile();
        int userId = getUserId(user);
        try {
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write(user.getName() + "," + (userId + 1));
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return userId;
    }

    @Override
    public int getUserId(final User user) {
        int userId = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                List<String> listLine = Arrays.stream(line.split("[,]")).toList();
                userId = Integer.parseInt(listLine.get(1));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userId;
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
}

