import com.google.gson.Gson;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchNumber implements NumberFinder{
    Logger logger;
    FastestComparator fastestComparator = new FastestComparator();

    public SearchNumber(){
        logger = LoggerFactory.getLogger(SearchNumber.class);
    }

    @Override
    public boolean contains(int valueToFind, List<CustomNumberEntity> list) {

        if (!list.isEmpty()) {
            logger.info("list not empty. Now processing");
            Optional<Integer> exist =
                    list.stream()
                            .parallel()
                            .map(customNumber -> {
                                try {
                                    return fastestComparator.compare(valueToFind, customNumber);
                                } catch (NumberFormatException e) {
                                    logger.error(e.toString());
                                    return Integer.MAX_VALUE;
                                }
                            })
                            .filter(a -> a.equals(0))
                            .findAny();
            return exist.isPresent();
        }
        logger.info("Empty list ! Returning false");
        return false;
    }

    @Override
    public List<CustomNumberEntity> readFromFile(String filePath) {
        Gson gson = new Gson();
        BufferedReader br = null;
        ArrayList<CustomNumberEntity> data = new ArrayList<>();
        try {
            // reading json file from file path
            br = new BufferedReader(new FileReader(filePath));

            //creating an array using gson -- fromJson
            CustomNumberEntity[] userArray = gson.fromJson(br, CustomNumberEntity[].class);

            //processing the list and filtering null and non numeric values.
            data = (ArrayList<CustomNumberEntity>) Arrays.stream(userArray)
                    .filter(value-> value.getNumber()!=null && NumberUtils.isNumber(value.getNumber()) )
                    .collect(Collectors.toList());

            br.close();
            return data;
        } catch(NullPointerException | JsonSyntaxException | IOException e) {
            //logging Exception and returning the list as blank
            logger.error(e.toString());
            return data;
        }

    }


    public static void main(String args[]){
        SearchNumber check = new SearchNumber();
        List<CustomNumberEntity> list = check.readFromFile(System.getProperty("user.dir") + "\\data.json");
        //false
        System.out.println(check.contains(90,list));
        //true
        System.out.println(check.contains(30,list));
    }
}
