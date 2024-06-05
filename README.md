# Spring Boot 3.0 
-> The application will be available at http://localhost:8080.


-> საიტზე გვყავს ორი ტიპის მოხმარებელი: შიდა(Internal) და გარე(External).
-> გვაქვს ფუნქციონალი რომელიც მოიაზრებს: სასტუმროების, მისი ოთახების და ტურების დამატებას, წაშლას, დათვალიერებას, განახლებას, დაჯავშნას, და ა.შ

-> ავტორიზაცია/რეგისტრაციის API ები:
-> InternalModule -> 
-> 1)/api/internal/auth/register | Post მეთოდი
-> შიდა მოხმარებლის შესაქმენალდ
-> json(RegisterInternalRequestDto) ში უნდა გავატანოთ:
        String firstname,
        String lastname,
        String email,
        String mobileNumber,
        String password,
	String image
ყველა ველი(გარდა სურათის) უნდა იყოს სავალდებულო.
პატერნები:
EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
PHONE_NUMBER_PATTERN = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
თუ მეილი ან ნომერი უკვე არსებობს დაბრუნდება კოდი 409 შეტყობინებით: "email_or_phone_number_exists"
2)/api/internal/auth/authenticate | Post მეთოდი
შიდა მოხმარებლის დასალოგინებლად
json(AuthenticationRequestDto)ში უნდა გავატანოთ:
        String email,
        String password
მეილის იგივე პატერნი რაც "1)" ში
ორივე წარმატების შემთხვევაში(რეგისტრაცია ავტორიცაზია) დაბრუნდება:
accessToken და refreshToken
ExternalModule -> 
1)/api/external/auth/countries | Get მეთოდი დაბრუნდება ქვეყნები რომელი რეგისტრაციისას უნდა მიუთითოს გარე მოხმარებელმა
2)/api/external/auth/register | Post მეთდი
შიდა მოხმარებლის შესაქმენალდ
json(RegisterExternalRequestDto)ში უნდა გავატანოთ:
        String firstname,
        String lastname,
        String email,
        String mobileNumber,
        String password,
        Country country,
        String image
ყველა ველი(გარდა სურათის) უნდა იყოს სავალდებულო.
პატერნები:
EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
PHONE_NUMBER_PATTERN = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
თუ მეილი ან ნომერი უკვე არსებობს დაბრუნდება კოდი 409 შეტყობინებით: "email_or_phone_number_exists"
3)/api/external/auth/authenticate | Post მეთოდი
გარე მოხმარებლის დასალოგინებლად
json(AuthenticationRequestDto)ში უნდა გავატანოთ:
        String email,
        String password
მეილის იგივე პატერნი რაც "2)" ში
ორივე წარმატების შემთხვევაში(რეგისტრაცია ავტორიცაზია) დაბრუნდება:
accessToken და refreshToken


სხვა API ები:
InternalModule -> Hotels მოცემული API ებისთვის საჭიროა მომხმარებლის ჰქონდეს როლი "INTERNAL_USER"
1)/internal/hotels/cities -> აბრუნებს ქალქების(საქართველოს ქალაქების) სიას | Get მეთოდი
2)/internal/hotels -> აბრუნებს სასტუმროების სიას | Get მეთოდი
შესაძლებელია ქალაქის მიხედვით დაფილტვრა, პარამეტრში სახელად city გავატანთ სასურველ ქალაქს, ასევე შესაძლებელია სახელით ძიება search პარამეტრის გამოყენებით
3)/internal/hotels/{id}/images -> აბრუნებს სასტუმროების ფოტოებს | Get მეთოდი
{id} - ის ადგილას ჩაიწერება სასტუმროს აიდი რომელი დაბრუნდება "2)" ში სხვა მონაცემებთან ერთად
4)/internal/hotels | Post მეთოდი ახალი ჩანაწერის შესაქმნელად
json(HotelCreateDto) ში უნდა გავატანოთ შემდეგი ველები:
        String name,
        String address,
        City city,
        String zipCode,
        String email,
        String phone,
        String description,
        List<String> imageUrls
ყველა ველი უნდა იყოს სავალდებულო.
პატერნები:
EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
PHONE_NUMBER_PATTERN = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
თუ მეილი ან ნომერი უკვე არსებობს დაბრუნდება კოდი 409 შეტყობინებით: "email_or_phone_number_exists"
5)/internal/hotels/{id} | Put მეთოდი ჩანაწერის განახლებისთვის
json(HotelCreateDto) ში ვატანთ იმავე ველებს იამვე პატერნით რაც "4" - ში
6)/internal/hotels/{id} | Delete მეთოდი ჩანაწერის წასაშლელად
7)/internal/hotels/{hotel-id}/rate | Post მეთოდი სასტუმროს შესაფასებლად.
იმ შემთხვევაში თუ მოხმარებელს არ აქვს დაჯავშნილი სასტუმროს ოთახი ვერ შეძლებს სასტუმროს შეფასებას და დაბრუნდება კოდი 409 შეტყობინებით: "cant_rate_this_hotel",
თუ პარამეტრად(rating) გაატანა რიცხვი 1 ზე ნაკლები ან 10ზე მეტი მაშინ დაბრუნდება კოდი 409 შეტყობინებით: "incorrect_rating" 
8)/internal/hotels/{hotel-id}/image | Delete მეთოდი 
უნდა გავატანოთ image პარამეტრად რომელიც წაიშლება
9)/internal/hotels/{hotel-id}/image | Patch მეთოდი 
იცვლება cover ის ფოტო(რაც სასტუმროების სიაში უნდა ჩანდეს კონკრეტული სასტუმროს არჩევამდე)
პარამეტრად უნდა გავატანოთ image
