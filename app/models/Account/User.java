package models.Account;


import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import utils.DbUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by harvey on 15-8-31.
 */
@Entity("user")
public class User  {

    public User(){
        for(int i=0;i<10;i++){
            result_id.add("");
        }
    }

    @Id
    private String id;

    private String email;

    private String password;

    private boolean validated = false;

    @Embedded("token")
    private Token token = new Token();


    @Embedded("result_id")
    private List<String> result_id = new ArrayList<String>();

    /*****************************************************************
                               some useful methods to access database
     *****************************************************************/
    public static User Authenticate(String email,String password){
        List<User> users = DbUtil.getDataStore().createQuery(User.class)
                .filter("email",email)
                .filter("password",password).asList();
        if(!users.isEmpty())
            return users.get(0);
        else
            return null;
    }

    public static User getUser(String email){
        List<User> users = DbUtil.getDataStore().createQuery(User.class)
                .filter("email", email).asList();
        if(!users.isEmpty()){
            return users.get(0);
        }else{
            return null;
        }
    }

    public static boolean saveUser(User user){
        DbUtil.getDataStore().save(user);
        return true;
    }

    public static boolean validEmail(String id){
        Datastore datastore = DbUtil.getDataStore();
        final Query<User> query = datastore.createQuery(User.class)
                .filter("_id",id);
        final UpdateOperations<User> updateOperation  = datastore.createUpdateOperations(User.class)
                .set("validated",true);

        final UpdateResults results = datastore.update(query, updateOperation);
        return true;
    }

    public static boolean updatePsw(String email,String psw){
        Datastore datastore = DbUtil .getDataStore();
        final Query<User> query =
                datastore.createQuery(User.class)
                .filter("email",email);
        final UpdateOperations<User> updateOperation  =
                datastore.createUpdateOperations(User.class)
                .set("password",psw);

        final UpdateResults results =
                datastore.update(query, updateOperation);
        return true;
    }

    public static boolean isAvailable(String id,String email){
        Datastore datastore = DbUtil.getDataStore();
        if (!datastore.createQuery(User.class)
                        .filter("_id", id).asList().isEmpty()||
                !datastore.createQuery(User.class)
                        .filter("email", email).asList().isEmpty())
            return false;
        else
            return true;
    }

    /*****************************************************************
                               setter and getter of all attributes
     ******************************************************************/

    public List<String> getResult_id() {
        return result_id;
    }

    public void setResult_id(List<String> result_id) {
        this.result_id = result_id;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map[] getAllToken(){
        return token.getAll();
    }
}
