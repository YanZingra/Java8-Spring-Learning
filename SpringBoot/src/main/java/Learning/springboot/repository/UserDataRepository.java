package Learning.springboot.repository;


import Learning.springboot.model.UserData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDataRepository {

    @Resource(name = "userList")
   // @Valid
    private List<UserData> dataList;

    public void saveUsers(@NotBlank @NotNull List<UserData> data) {

        var dataExists = false;

        for (UserData userData : data) {
            if (dataList.stream().anyMatch(d -> d.getRg().equals(userData.getRg()))) {
                dataExists = true;
                break;
            }
        }
        if (!dataExists) {
            dataList.addAll(data);
        } else {
            throw new KeyAlreadyExistsException("Este RG já foi registrado no sistema senhor, ta tentando me enganar?");
        }
    }

    public Boolean deleteUser(Integer id) {
        List<UserData> dataListToDelete = this.dataList.stream()
                .filter(data -> data.getAge().equals(id)).collect(Collectors.toList());
        return dataList.removeAll(dataListToDelete);
    }

    public List<UserData> getUser() {
        return dataList;
    }

    public UserData getUser(Integer rg) {

        List<UserData> users = dataList.stream().filter(d -> d.getRg().equals(rg)).collect(Collectors.toList());

        return users.isEmpty() ? null : users.get(0);
    }

    public List<UserData> getUserById(Integer rg) {

        return dataList.stream()
                .filter(data -> data.getRg().equals(rg))
                .collect(Collectors.toList());
    }

    public void changeUser(UserData data, Integer rg) {
        dataList = dataList.stream()
                .map(d -> {
                    if (d.getRg().equals(rg))
                        return data;
                    return d;
                })
                .collect(Collectors.toList());
    }

    public void partChangeUser(UserData data, Integer rg) {
        dataList.forEach(d -> {
            if (d.getRg().equals(rg)) {
                if (data.getName() != null) d.setName(data.getName());
                if (data.getAge() != null) d.setAge(data.getAge());
                if (data.getRg() != null) d.setRg(data.getRg());
            }
        });
    }
}