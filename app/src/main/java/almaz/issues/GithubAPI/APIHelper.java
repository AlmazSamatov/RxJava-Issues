package almaz.issues.GithubAPI;

import java.util.List;

import almaz.issues.ObjectClasses.Comment;
import almaz.issues.ObjectClasses.Issue;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by almaz on 23.04.17.
 */

public interface APIHelper {

    @GET("repos/{owner}/{repository}/issues")
    Call<List<Issue>> getIssues(@Path("owner") String owner,
                                @Path("repository") String repository);

    @GET("repos/{owner}/{repository}/issues/{number}")
    Call<Issue> getIssue(@Path("owner") String owner,
                         @Path("repository") String repository,
                         @Path("number") int number);

    @GET
    Call<List<Comment>> getComments(@Url String comments_url);


}