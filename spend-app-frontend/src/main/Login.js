import React, { useState } from 'react';
import { Button, CircularProgress, Container, CssBaseline, TextField, Typography } from '@mui/material';
import { makeStyles } from "@mui/styles";
import { useTheme } from '@mui/material/styles';
import axios from 'axios';
import { COMMON_URL } from '../constants/URL';
import { Link, useNavigate } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import { useDispatch } from 'react-redux';
import { bindActionCreators } from 'redux';
import { actionCreators } from '../state/ActionCreator';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const useStyles = makeStyles(() => ({
    root: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh',
        //background: 'linear-gradient(45deg, #FE6B8B 30%, #FF8E53 90%)',
        backgroundSize: 'cover',
    },
    form: {
        width: '100%',
        maxWidth: '400px',
        padding: useTheme().spacing(2),
        //background: 'linear-gradient(10deg, #FE6B8B 10%, #FF8E53 90%)',
        borderRadius: '8px',
        textAlign: 'center',
    },
    title: {
        marginBottom: useTheme().spacing(2),
    },
    input: {
        marginBottom: useTheme().spacing(2),
    },
    submitButton: {
        backgroundColor: '#FF8E53',
        color: '#fff',
        '&:hover': {
            backgroundColor: '#FE6B8B',
        },
    },
    blurBackground: {
        backdropFilter: 'blur(5px)', // Adjust the blur strength as needed
        backgroundColor: 'rgba(255, 255, 255, 0.5)', // Adjust the background color and opacity as needed
        position: 'absolute',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        zIndex: useTheme().zIndex.modal + 1, // Ensure this value is higher than other elements
    },
}));

const Login = () => {
    const navigate = useNavigate();
    const classes = useStyles();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [cookies, setCookie] = useCookies(['access_token']);
    const [isLoading, setIsLoading] = useState(false);
    const dispatch = useDispatch();
    const actions = bindActionCreators(actionCreators, dispatch);

    const handleLogin = async (e) => {
        if (username === '' || password === '') {
            toast.error('Username and password are required', {
                position: toast.POSITION.TOP_RIGHT,
                style: { backgroundColor: 'red', color: '#fff' },
            });
            return;
        }
        e.preventDefault();
        setIsLoading(true);
        try {
            const credentials = { username: username, password: password };
            const response = await axios.post(COMMON_URL + 'auth/authenticate', credentials);
            if (response.status === 200) {
                let expires = new Date();
                expires.setTime(expires.getTime() + (1000 * 60 * 60 * 24 * 2))
                navigate('/dashboard');
                var token = 'Bearer ' + response.data.token;
                setCookie('access_token', token, { expires });
                actions.loginAction(response.data.userDetails);
            }
            else {
                toast.error('Incorrect Username or password.', {
                    position: toast.POSITION.TOP_RIGHT,
                    style: { backgroundColor: 'red', color: '#fff' },
                });
                setIsLoading(false);
            }
        } catch (error) {
            console.error('Login failed:', error);
            toast.error('Incorrect Username or password.', {
                position: toast.POSITION.TOP_RIGHT,
                style: { backgroundColor: 'red', color: '#fff' },
            });
            setIsLoading(false);
        }
    };

    const handlePasswordKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleLogin(e);
        }
    };

    return (
        <div className={classes.root}>
            {isLoading && <div className={classes.blurBackground}><CircularProgress /></div>}
            <Container component="main" maxWidth="xs" color='primary'>
                <CssBaseline />
                <div className={classes.form}>
                    <Typography variant="h5" className={classes.title}>
                        Login
                    </Typography>
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        label="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        className={classes.input}
                        onKeyPress={handlePasswordKeyPress}
                    />
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        label="Password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className={classes.input}
                        onKeyPress={handlePasswordKeyPress}
                    />
                    <Button
                        fullWidth
                        variant="contained"
                        className={classes.submitButton}
                        onClick={handleLogin}
                    >
                        Login
                    </Button>
                    <Link to="/signup" className={classes.link}>
                        Don't have an account? Sign Up
                    </Link>
                </div>
            </Container>
        </div>
    );
};

export default Login;
