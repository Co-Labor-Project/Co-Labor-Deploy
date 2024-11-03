import axios from 'axios';

export const fetchData = async ({
  url,
  setEnterprises,
  setJobs,
  setLoading,
}) => {
  try {
    const response = await axios.get(url, {
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      withCredentials: true,
    });

    const data = response.data;
    setEnterprises(data.enterprises || []);
    setJobs(data.jobs || []);
    setLoading(false);
  } catch (error) {
    console.error('Fetch error:', error);
    setLoading(false);
  }
};
