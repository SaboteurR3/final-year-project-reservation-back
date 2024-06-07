# Spring Boot 3.0
The application will be available at [http://localhost:8080](http://localhost:8080).
Swagger: http://localhost:8080/swagger/swagger-ui/index.html

საიტზე გვყავს ორი ტიპის მოხმარებელი: შიდა(Internal) და გარე(External). გვაქვს ფუნქციონალი რომელიც მოიაზრებს: სასტუმროების, მისი ოთახების და ტურების დამატებას, წაშლას, დათვალიერებას, განახლებას, დაჯავშნას, და ა.შ.

## ავტორიზაცია/რეგისტრაციის API ები:

### InternalModule
1. **POST /api/internal/auth/register**  
   შიდა მოხმარებლის შესაქმენალდ  
   json(RegisterInternalRequestDto) ში უნდა გავატანოთ:
   - `String firstname`
   - `String lastname`
   - `String email`
   - `String mobileNumber`
   - `String password`
   - `String image`

   ყველა ველი(გარდა სურათის) უნდა იყოს სავალდებულო.  
   პატერნები:  
   - EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
   - PHONE_NUMBER_PATTERN = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";

   თუ მეილი ან ნომერი უკვე არსებობს დაბრუნდება კოდი 409 შეტყობინებით: "email_or_phone_number_exists"

2. **POST /api/internal/auth/authenticate**  
   შიდა მოხმარებლის დასალოგინებლად  
   json(AuthenticationRequestDto)ში უნდა გავატანოთ:
   - `String email`
   - `String password`

   მეილის იგივე პატერნი რაც "1)" ში  
   ორივე წარმატების შემთხვევაში (რეგისტრაცია ავტორიცაზია) დაბრუნდება: accessToken და refreshToken

### ExternalModule
1. **GET /api/external/auth/countries**  
   დაბრუნდება ქვეყნები რომელი რეგისტრაციისას უნდა მიუთითოს გარე მოხმარებელმა

2. **POST /api/external/auth/register**  
   შიდა მოხმარებლის შესაქმენალდ  
   json(RegisterExternalRequestDto)ში უნდა გავატანოთ:
   - `String firstname`
   - `String lastname`
   - `String email`
   - `String mobileNumber`
   - `String password`
   - `Country country`
   - `String image`

   ყველა ველი (გარდა სურათის) უნდა იყოს სავალდებულო.  
   პატერნები:  
   - EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
   - PHONE_NUMBER_PATTERN = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";

   თუ მეილი ან ნომერი უკვე არსებობს დაბრუნდება კოდი 409 შეტყობინებით: "email_or_phone_number_exists"

3. **POST /api/external/auth/authenticate**  
   გარე მოხმარებლის დასალოგინებლად  
   json(AuthenticationRequestDto)ში უნდა გავატანოთ:
   - `String email`
   - `String password`

   მეილის იგივე პატერნი რაც "2)" ში  
   ორივე წარმატების შემთხვევაში (რეგისტრაცია ავტორიცაზია) დაბრუნდება: accessToken და refreshToken

## სხვა API ები:

### InternalModule
Hotels მოცემული API ებისთვის საჭიროა მომხმარებლის ჰქონდეს როლი "INTERNAL_USER"
1. **GET /internal/hotels/cities**  
   აბრუნებს ქალქების(საქართველოს ქალაქების) სიას

2. **GET /internal/hotels**  
   აბრუნებს სასტუმროების სიას  
   შესაძლებელია ქალაქის მიხედვით დაფილტვრა, პარამეტრში სახელად city გავატანთ სასურველ ქალაქს, ასევე შესაძლებელია სახელით ძიება search პარამეტრის გამოყენებით

3. **GET /internal/hotels/{id}/images**  
   აბრუნებს სასტუმროების ფოტოებს  
   {id} - ის ადგილას ჩაიწერება სასტუმროს აიდი რომელი დაბრუნდება "2)" ში სხვა მონაცემებთან ერთად

4. **POST /internal/hotels**  
   ახალი ჩანაწერის შესაქმნელად  
   json(HotelCreateDto) ში უნდა გავატანოთ შემდეგი ველები:
   - `String name`
   - `String address`
   - `City city`
   - `String zipCode`
   - `String email`
   - `String phone`
   - `String description`
   - `List<String> imageUrls`

   ყველა ველი უნდა იყოს სავალდებულო.  
   პატერნები:  
   - EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
   - PHONE_NUMBER_PATTERN = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";

   თუ მეილი ან ნომერი უკვე არსებობს დაბრუნდება კოდი 409 შეტყობინებით: "email_or_phone_number_exists"

5. **PUT /internal/hotels/{id}**  
   ჩანაწერის განახლებისთვის  
   json(HotelCreateDto) ში ვატანთ იმავე ველებს იამვე პატერნით რაც "4" - ში

6. **DELETE /internal/hotels/{id}**  
   ჩანაწერის წასაშლელად

7. **POST /internal/hotels/{hotel-id}/rate**  
   სასტუმროს შესაფასებლად.  
   იმ შემთხვევაში თუ მოხმარებელს არ აქვს დაჯავშნილი სასტუმროს ოთახი ვერ შეძლებს სასტუმროს შეფასებას და დაბრუნდება კოდი 409 შეტყობინებით: "cant_rate_this_hotel",  
   თუ პარამეტრად(rating) გაატანა რიცხვი 1 ზე ნაკლები ან 10ზე მეტი მაშინ დაბრუნდება კოდი 409 შეტყობინებით: "incorrect_rating" 

8. **DELETE /internal/hotels/{hotel-id}/image**  
   უნდა გავატანოთ image პარამეტრად რომელიც წაიშლება

9. **PATCH /internal/hotels/{hotel-id}/image**  
   იცვლება cover ის ფოტო (რაც სასტუმროების სიაში უნდა ჩანდეს კონკრეტული სასტუმროს არჩევამდე)  
   პარამეტრად უნდა გავატანოთ image
