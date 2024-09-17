import React, { useState } from 'react';
import { Button, CircularProgress, Container, CssBaseline, TextField, Typography, Fade } from '@mui/material';
import { makeStyles } from "@mui/styles";
import { useTheme } from '@mui/material/styles';
import axios from 'axios';
import { COMMON_URL } from '../constants/URL';
import { Link, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const useStyles = makeStyles(() => ({
    root: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh',
        backgroundSize: 'cover',
    },
    form: {
        width: '100%',
        maxWidth: '400px',
        padding: useTheme().spacing(2),
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
        backdropFilter: 'blur(5px)',
        backgroundColor: 'rgba(255, 255, 255, 0.5)',
        position: 'absolute',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        zIndex: useTheme().zIndex.modal + 1,
    },
}));

const SignUp = () => {
    const navigate = useNavigate();
    const classes = useStyles();
    const [username, setUsername] = useState('');
    const [fullName, setFullName] = useState('');
    const [emailId, setEmailId] = useState('');
    const [password, setPassword] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [otp, setOtp] = useState('');
    const [isOtpSent, setIsOtpSent] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    const handleVerifyOTP = async () => {
        if (!otp) {
            toast.error('OTP is required', {
                position: toast.POSITION.TOP_RIGHT,
                style: { backgroundColor: 'red', color: '#fff' },
            });
            return;
        }

        setIsLoading(true);
        try {
            const userDetails = { username: username, otp: otp };
            const response = await axios.post(COMMON_URL + 'auth/verifyotp', userDetails);
            if (response.status === 200 && response.data.status) {
                toast.success(response.data.message, {
                    position: toast.POSITION.TOP_RIGHT,
                    style: { backgroundColor: 'green', color: '#fff' },
                });
                navigate('/login');
            } else {
                toast.error(response.data.message, {
                    position: toast.POSITION.TOP_RIGHT,
                    style: { backgroundColor: 'red', color: '#fff' },
                });
            }
        } catch (error) {
            console.error('Failed to send OTP:', error);
            toast.error('Failed to verify OTP. Please try again.', {
                position: toast.POSITION.TOP_RIGHT,
            });
        } finally {
            setIsLoading(false);
        }
    };

    const handleSignUp = async (e) => {
        e.preventDefault();

        if (!username || !fullName || !emailId || !password || !phoneNumber) {
            toast.error('All fields are required', {
                position: toast.POSITION.TOP_RIGHT,
                style: { backgroundColor: 'red', color: '#fff' },
            });
            return;
        }

        if (!validateEmail(emailId)) {
            toast.error('Invalid email address', {
                position: toast.POSITION.TOP_RIGHT,
                style: { backgroundColor: 'red', color: '#fff' },
            });
            return;
        }

        if (!validatePhoneNumber(phoneNumber)) {
            toast.error('Invalid phone number', {
                position: toast.POSITION.TOP_RIGHT,
                style: { backgroundColor: 'red', color: '#fff' },
            });
            return;
        }

        setIsLoading(true);
        try {
            const userDetails = { username: username, emailId: emailId, password: password, phoneNumber: phoneNumber, fullName: fullName };
            const response = await axios.post(COMMON_URL + 'auth/signup', userDetails);
            if (response.status === 200 && response.data.status) {
                toast.success(response.data.message, {
                    position: toast.POSITION.TOP_RIGHT,
                    style: { backgroundColor: 'green', color: '#fff' },
                });
                setIsOtpSent(true);
            } else {
                toast.error(response.data.message, {
                    position: toast.POSITION.TOP_RIGHT,
                    style: { backgroundColor: 'red', color: '#fff' },
                });
            }
        } catch (error) {
            console.error('Signup failed:', error);
            toast.error('Signup failed. Please try again.', {
                position: toast.POSITION.TOP_RIGHT,
                style: { backgroundColor: 'red', color: '#fff' },
            });
        } finally {
            setIsLoading(false);
        }
    };

    const validateEmail = (email) => {
        const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return re.test(String(email).toLowerCase());
    };

    const validatePhoneNumber = (phone) => {
        const re = /^\d{10}$/;
        return re.test(String(phone));
    };

    const handleChange = (e) => {
        const value = e.target.value;
        // Allow only integers and maximum length of 6
        if (/^\d*$/.test(value) && value.length <= 6) {
            setOtp(value);
        }
    };

    return (
        <div className={classes.root}>
            {isLoading && <div className={classes.blurBackground}><CircularProgress /></div>}
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <div className={classes.form}>
                    <Typography variant="h5" className={classes.title}>
                        Sign Up
                    </Typography>
                    <Fade in={!isOtpSent}>
                        <div>
                            {!isOtpSent && (
                                <>
                                    <TextField
                                        variant="outlined"
                                        margin="normal"
                                        required
                                        fullWidth
                                        label="Username"
                                        value={username}
                                        onChange={(e) => setUsername(e.target.value)}
                                        className={classes.input}
                                    />
                                    <TextField
                                        variant="outlined"
                                        margin="normal"
                                        required
                                        fullWidth
                                        label="Full Name"
                                        value={fullName}
                                        onChange={(e) => setFullName(e.target.value)}
                                        className={classes.input}
                                    />
                                    <TextField
                                        variant="outlined"
                                        margin="normal"
                                        required
                                        fullWidth
                                        label="Email"
                                        value={emailId}
                                        onChange={(e) => setEmailId(e.target.value)}
                                        className={classes.input}
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
                                    />
                                    <TextField
                                        variant="outlined"
                                        margin="normal"
                                        required
                                        fullWidth
                                        label="Phone Number"
                                        value={phoneNumber}
                                        onChange={(e) => setPhoneNumber(e.target.value)}
                                        className={classes.input}
                                    />
                                    <Button
                                        fullWidth
                                        variant="contained"
                                        className={classes.submitButton}
                                        onClick={handleSignUp}
                                    >
                                        SignUp
                                    </Button>
                                </>
                            )}
                        </div>
                    </Fade>
                    <Fade in={isOtpSent}>
                        <div>
                            {isOtpSent && (
                                <>
                                    <TextField
                                        variant="outlined"
                                        margin="normal"
                                        required
                                        fullWidth
                                        label="OTP"
                                        value={otp}
                                        onChange={handleChange}
                                        className={classes.input}
                                        inputProps={{ maxLength: 6 }}
                                    />
                                    <Button
                                        fullWidth
                                        variant="contained"
                                        className={classes.submitButton}
                                        onClick={handleVerifyOTP}
                                    >
                                        verifyOtp
                                    </Button>
                                </>
                            )}
                        </div>
                    </Fade>
                    <Link to="/login" className={classes.link}>
                        Have an account? Log In
                    </Link>
                </div>
            </Container>
        </div>
    );
};

export default SignUp;