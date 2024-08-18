#include "TagRestaurantFilterJSON.hpp"

#include <string>

#include <lib/error_description.hpp>

#include <userver/utils/time_of_day.hpp>
#include <userver/formats/parse/common_containers.hpp>


namespace service {


    const std::string TagRestaurantFilterJSON::kFieldName_ = "tags";
    const std::unordered_map<std::string, std::string> TagRestaurantFilterJSON::kCorrectOperators_ = {
            {"in", " {} && ${}::varchar(50)[] "},
            {"not in", " NOT ( {} && ${}::varchar(50)[] )"},
    };
    const std::unordered_set<std::string> TagRestaurantFilterJSON::kCorrectTags_ = {
            "Можно с собаками",
            "Азиатская",
            "Подача от шефа",
            "Танцпол",
            "Есть доставка",
            "Есть доставка Ultima",
            "Мексиканская",
            ">5000 ₽",
            "Паназиатская",
            "Детская анимация",
            "Сербская",
            "Бар",
            "Банкетный зал",
            "Встреча с друзьями",
            "8 марта",
            "Чайхана",
            "Кофейня",
            "Турецкая",
            "Домашняя",
            "Вегетарианская",
            "Итальянская",
            "Вьетнамская",
            "2500-5000 ₽",
            "Коктейли",
            "Молекулярная",
            "Индийская",
            "Фудмолл",
            "Пиццерия",
            "Узбекская",
            "Рыба и морепродукты",
            "Комфорт-фуд",
            "Бургерная",
            "Латиноамериканская",
            "Детская комната",
            "Чайная",
            "Чешская",
            "Русская",
            "Кальянная",
            "Винотека",
            "Арабская",
            "Выбор редакции",
            "Мультикухня",
            "Можно с животными",
            "Можно с животными до 5 кг",
            "Китайская",
            "1000-2500 ₽",
            "Балканская",
            "Завтрак",
            "Веранда",
            "Находится в бц",
            "Английская",
            "Спортивные трансляции",
            "Бесплатная парковка",
            "Корейская",
            "Пончиковая",
            "Сет-меню",
            "Восточноевропейская",
            "Нельзя с животными",
            "Рюмочная",
            "Азербайджанская",
            "Испанская",
            "Французская",
            "Тайская",
            "Можно с собаками до 5 кг",
            "Бизнес-ланч",
            "Живая музыка",
            "Гриль",
            "Бурятская",
            "Ресторан",
            "Киоск",
            "Кафе",
            "Кальянное меню",
            "Греческая",
            "Ирландская",
            "Халал",
            "Паб",
            "<1000 ₽",
            "Крафтовое пиво",
            "Американская",
            "Кавказская",
            "Еврейская",
            "Болгарская",
            "Японская",
            "Хинкальная",
            "Европейская",
            "Бельгийская",
            "Детское меню",
            "Находится в тц",
            "Грузинская",
            "Ближневосточная",
            "ULTIMA GUIDE",
            "Открытая кухня",
    };

    std::variant<std::string, ErrorDescriprion> TagRestaurantFilterJSON::BuildSQLFilter(
            userver::storages::postgres::ParameterStore& params,
            const userver::formats::json::Value& JSON
    )
    {
        const auto& op = JSON["operator"].As<std::string>();
        if (!kCorrectOperators_.count(op)) {
            return ErrorDescriprion::kInvalidOperator;
        }

        for (const auto& tag : JSON["value"]) {
            if (!kCorrectTags_.count(tag.As<std::string>())) {
                return ErrorDescriprion::kInvalidValueType;
            }
        }

        params.PushBack(JSON["value"].As< std::vector<std::string> >());
        return fmt::format(
                kCorrectOperators_.at(op),
                kFieldName_,
                params.Size()
        );
    }


} // namespace service