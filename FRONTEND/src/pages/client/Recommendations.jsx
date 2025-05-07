import React from 'react';
import { useParams } from 'react-router-dom';
import { getEventById } from '../../api/eventApi';

const Recommendations = () => {
  const { eventId } = useParams();
  const [event, setEvent] = React.useState(null);

  React.useEffect(() => {
    (async () => {
      const response = await getEventById(eventId);
      setEvent(response.data);
    })();
  }, [eventId]);

  if (!event) {
    return null;
  }

  return (
    <div>
      <h1>Recommendations for {event.name}</h1>
      <p>TODO: fetch recommendations from API</p>
    </div>
  );
};

export default Recommendations;
