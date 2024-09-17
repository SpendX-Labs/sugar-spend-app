import React, { useState } from 'react';
import { makeStyles } from '@mui/styles';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import { COMMON_URL } from '../../constants/URL';

const useStyles = makeStyles({
    container: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        width: '100%',
        height: '50vh', // Adjust the height to fill the viewport
    },
});

const RecentTrans = (props) => {
    const [cookies, _setCookie] = useCookies(['access_token']);
    const [data, setData] = useState([]);
    const classes = useStyles();

    const fetchData = async () => {
        try {
            axios.defaults.headers.common['Authorization'] = cookies['access_token'];
            // var reqMonth = "";
            // if (!props.disableMonth)
            //     reqMonth = props.selectedMonth;
            // const reqData = { budgetYear: props.selectedYear, budgetMonth: reqMonth };
            // const res = await axios.post(COMMON_URL + "app/get-pie-chart", reqData);
            // const totalData = await axios.post(COMMON_URL + "app/get-total-amount", reqData);
            // if (res.status === 200 && totalData.status === 200) {
            //     setData(res.data);
            //     setTotalAmount(totalData.data["totalSpend"]);
            // }
        } catch (error) {
            // Handle errors if any of the API calls fail
            console.error('Error calling one or more APIs', error);
        }
    }

    React.useEffect(() => {
        fetchData();
    }, [props.selectedMonth, props.selectedYear, props.isSaved, props.disableMonth]);

    return (
        <div className={classes.container}>
            Hi

        </div>
    );
};

export default RecentTrans;
