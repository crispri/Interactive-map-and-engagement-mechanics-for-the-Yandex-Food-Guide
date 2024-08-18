import styles from './HeaderFilters.module.scss'
import Filter from './Filter'
import { useNavigate } from 'react-router-dom';
import { useSelector } from "react-redux";
import filtersButton from '../../assets/filters_button.svg'

const HeaderFilters = ({debouncedValue}) => {

    const navigateTo = useNavigate();
    
    function handleFilterFullViewClick() {
        navigateTo("/restaurants/filters");
    }

    const filtersMap = useSelector((state) => state.restaurantsSlice.filters)

    const filters = [
        {
            key: "nearby",
            name: "Рядом со мной",
            operator: "test1"
        },
        {
            key: "open_now",
            name: "Открыто сейчас",
            operator: "test2"
        },
        {
            key: "ultima",
            name: "ULTIMA",
            operator: "test3"
        },
        {
            key: "ultima2",
            name: "ULTIMA2",
            operator: "test3"
        },
        {
            key: "ultima3",
            name: "ULTIMA3",
            operator: "test3"
        }
    ]

    return (
        <>
            <div className={styles.filters}>
                <img className={styles.filters_button} src={filtersButton} alt="Filter button" onClick={handleFilterFullViewClick}></img>
                {filters?.map((filter, index) => (
                    <Filter key={index} filtersMap={filtersMap} debouncedValue={debouncedValue} filter={filter}></Filter>
                ))}
            </div>
        </>
    )
}

export default HeaderFilters;