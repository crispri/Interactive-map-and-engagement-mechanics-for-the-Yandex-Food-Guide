import styles from './HeaderFilters.module.scss'
import Filter from './Filter'
import { useNavigate } from 'react-router-dom';
import { useSelector } from "react-redux";
import filtersButton from '../../assets/filters_button.svg'

const HeaderFilters = ({debouncedValue}) => {

    const navigateTo = useNavigate();
    const filtersMap = useSelector((state) => state.filters);
    
    function handleFilterFullViewClick() {
        navigateTo("/restaurants/filters");
    }

    const filters = [
        {
            name: "Рядом со мной",
            operator: "test1"
        },
        {
            name: "Открыто сейчас",
            operator: "test2"
        },
        {
            name: "ULTIMA",
            operator: "test3"
        }
    ]

    return (
        <>
            <div className={styles.filters}>
                <img className={styles.filters_button} src={filtersButton} alt="Filter button" onClick={handleFilterFullViewClick}></img>
                {filters?.map((filter, index) => (
                    <div key={index} className={styles.filter}><Filter filtersMap={filtersMap} debouncedValue={debouncedValue} filter={filter}></Filter></div>
                ))}
            </div>
        </>
    )
}

export default HeaderFilters;